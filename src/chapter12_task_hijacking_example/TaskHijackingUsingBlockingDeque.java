package src.chapter12_task_hijacking_example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
10개의 worker thread가 존재하고, 이들 각각은 자신만의 BlockingDeque을 가지고 있다.

1000 개의 작업들이 10개의 worker thread들한테 배분 되는데, 특정 스레드 2개한테 작업들이 훨씬 많이 분배된다.

작업 한 개를 처리하는 것에 걸리는 시간은 1밀리초 ~ 10밀리초 이고, 이는 작업을 처리하기 시작한 시점에
ThreadLocalRandom 난수에 의해서 결정된다.

워커 스레드는 각자의 BlockingDeque에 저장된 작업들을 꺼내서 처리한다.
특정 컨슈머 스레드가 자신의 BlockingDeque이 텅 비게 된다면, 노는 것이 아니라 다른 컨슈머 스레드의 덱에서
작업을 빼와서 처리 한다.

하나의 BlockingQueue에서 여러 스레드들이 작업을 빼 가려고 경쟁하지 않으므로 BlockingQueue를 사용한 구현보다
안정성 측면에서 장점이 있다.

실행 결과, 처음에 작업이 균등하게 분배돼 있지 않아도 결과적으로 1개 스레드당 실행한 작업 개수는 1000개 내외로 비슷했고,
전체 실행시간도 BlockingQueue와 비슷한 5400~5600밀리초였다.
*/
public class TaskHijackingUsingBlockingDeque {
    public void run(){
        int[] tasksCountArr = {3000, 500, 500, 500, 500, 500, 500, 3000, 900, 100};

        List<BlockingDeque<Task>> blockingDequeList = new ArrayList<>();
        for(int i=0; i<10; i++){
            blockingDequeList.add(new LinkedBlockingDeque<>());
        }

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(10);

        int totalTaskCnt = 0;
        for (int i=0; i<tasksCountArr.length; i++) {
            Worker worker = new Worker(blockingDequeList, blockingDequeList.get(i), startGate, endGate);
            for (int j = 1; j <= tasksCountArr[i]; j++) {
                worker.putTaskInBlockingDeque(new Task());
                totalTaskCnt++;
            }

            Thread workerThread = new Thread(worker);
            workerThread.start();
        }

        System.out.println("작업 시작 직전 작업 개수 총합 : " + totalTaskCnt);

        try{
            long start = System.currentTimeMillis();
            startGate.countDown();
            endGate.await();
            long end = System.currentTimeMillis();
            System.out.println("블록킹덱 작업가로채기 패턴으로 병렬처리 했을 때 걸린 시간 밀리초 : " + (end-start));
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }//run()

    private final static class Worker implements Runnable {
        private final List<BlockingDeque<Task>> blockingDequeList;
        private final BlockingDeque<Task> taskBlockingQueue;
        private final CountDownLatch startGate;
        private final CountDownLatch endGate;
        private Worker(
                List<BlockingDeque<Task>> blockingDequeList, BlockingDeque<Task> taskBlockingQueue,
                CountDownLatch startGate,
                CountDownLatch endGate
        ) {
            this.blockingDequeList = blockingDequeList;
            this.taskBlockingQueue = taskBlockingQueue;
            this.startGate = startGate;
            this.endGate = endGate;
        }
        @Override
        public void run() {
            long taskDoneCnt = 0;
            long start = 0L;
            long end = 0L;
            try {
                startGate.await();
                start = System.currentTimeMillis();
                while(true){
                    if(!taskBlockingQueue.isEmpty()){
                        // 자신의 블록킹 덱에서 일을 먼저 처리한다.
                        taskBlockingQueue.take().work();
                        taskDoneCnt++;
                    } else break;
                }//while

                //System.out.println("자기 작업 완료 : " + Thread.currentThread().getId());

                // 자기꺼 다 처리했으면 다른 스레드의 블록킹 덱들의 작업들도 빼내서 처리한다.
                while(true){
                    int emptyDequeCnt = 0;
                    for(BlockingQueue<Task> otherDeque : blockingDequeList){
                        if(!otherDeque.isEmpty()){
                            otherDeque.take().work();
                            taskDoneCnt++;
                        } else {
                            emptyDequeCnt++;
                        }
                    }
                    if(emptyDequeCnt == blockingDequeList.size()) break;
                }
                end = System.currentTimeMillis();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                endGate.countDown();
                System.out.println(
                        "컨슈머스레드 아이디/처리한 task 개수/걸린 시간 밀리초 : "
                                + Thread.currentThread().getId()
                                +" / "+taskDoneCnt
                                +" / "+(end-start)
                );
            }
        }//run()

        public void putTaskInBlockingDeque(Task task){
            taskBlockingQueue.offerLast(task);
        }
    }//end of Producer

    private final static class Task {
        public void work(){
            try {
                int timeToNeed = ThreadLocalRandom.current().nextInt(1,11);
                Thread.sleep(timeToNeed);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }//end of task

}//end of class
