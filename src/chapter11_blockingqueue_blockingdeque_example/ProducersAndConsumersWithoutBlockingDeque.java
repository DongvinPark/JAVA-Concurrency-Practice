package src.chapter11_blockingqueue_blockingdeque_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
10 개의 프로듀서 스레드들이 각각 1000 개의 작업을 BlockingQueue에 집어 넣고,
이와 동시에 10개의 컨슈머 스레드들이 BlockingQueue의 작업들을 꺼내서 처리한다.

작업 한 개를 처리하는 것에 걸리는 시간은 1밀리초 ~ 10밀리초 이고, 이는 작업을 처리하기 시작한 시점에
ThreadLocalRandom 난수에 의해서 결정된다.
이때 컨슈머스레드의 아이디가 홀수일 경우, 랜덤 난수에 *2를 해서 고의적으로 이쪽의 작업시간을 더 길게 만든다.

컨슈머 스레드들을 자기가 가져온 작업을 처리하기만 할 뿐, 다른 컨슈머 스레드들의 작업 처리에는 관여하지 않는다.
1 개의 컨슈머 스레드는 최대 1000개 까지의 작업만을 처리할 수 있고, 그 후에는 더 이상 작업을 처리하지 못한다.
이렇게 설정한 이유는 특정 스레드에 시간이 더 오래 걸리는 작업들이 몰려 있어서 이쪽 스레드들이 병목 현상의 원인이 된 경우를 재현하기 위해서이다.
*/
public class ProducersAndConsumersWithoutBlockingDeque {
    public void run(){
        BlockingQueue<Task> taskBlockingQueue = new LinkedBlockingQueue<>();
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(10);

        for(int i=1; i<=10; i++){
            Thread producerThread = new Thread(new Producer(taskBlockingQueue));
            producerThread.start();
        }

        for(int i=1; i<=10; i++){
            Thread consumerThread = new Thread(new Consumer(
                taskBlockingQueue, startGate, endGate
            ));
            consumerThread.start();
        }

        while(true){
            if(taskBlockingQueue.size() == 10_000){
                System.out.println("작업처리 직전 블록킹큐 size : " + taskBlockingQueue.size());
                break;
            }
        }

        try {
            long start = System.currentTimeMillis();
            startGate.countDown();
            endGate.await();
            long end = System.currentTimeMillis();
            System.out.println("블로킹덱 없이 병렬 처리했을 때 걸린 시간 밀리초 : " + (end-start));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }//run()

    private final static class Producer implements Runnable {
        private final BlockingQueue<Task> taskBlockingQueue;
        private Producer(BlockingQueue<Task> taskBlockingQueue) {
            this.taskBlockingQueue = taskBlockingQueue;
        }
        @Override
        public void run() {
            for(int i=1; i<=1000; i++){
                taskBlockingQueue.add(new Task());
            }
        }
    }//end of Producer

    private final static class Consumer implements Runnable {
        private final BlockingQueue<Task> taskBlockingQueue;
        private final CountDownLatch startGate;
        private final CountDownLatch endGate;
        private Consumer(
            BlockingQueue<Task> taskBlockingQueue,
            CountDownLatch startGate,
            CountDownLatch endGate
        ) {
            this.taskBlockingQueue = taskBlockingQueue;
            this.startGate = startGate;
            this.endGate = endGate;
        }
        @Override
        public void run() {
            int taskDoneCnt = 0;
            long start = 0L;
            long end = 0L;
            try {
                start = System.currentTimeMillis();
                startGate.await();
                while(true){
                    if(!taskBlockingQueue.isEmpty() && taskDoneCnt < 1000){
                        taskBlockingQueue.take().work();
                        taskDoneCnt++;
                    } else break;
                }
                end = System.currentTimeMillis();
            } catch (InterruptedException e){
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
        }
    }

    private final static class Task {
        public void work(){
            try {
                int timeToNeed = ThreadLocalRandom.current().nextInt(1,11);
                if(Thread.currentThread().getId() % 2 == 1){
                    timeToNeed = timeToNeed*2;
                }
                Thread.sleep(timeToNeed);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }//end of task

}//end of class
