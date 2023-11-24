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

컨슈머 스레드들을 자기가 가져온 작업을 처리하기만 할 뿐, 다른 컨슈머 스레드들의 작업 처리에는 관여하지 않는다.

이러한 구조를 선택했을 경우 구현은 쉽지만, BlockingQueue가 Single Point of Failure 가 되고,
BlockingQueue가 스레드 세이프하지만 해당 큐로 여러 스레드가 동시에 접근하기 때문에 안정성 측면에서 약점이 존재한다.
*/
public class ProducersAndConsumersWithoutBlockingQueue {
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
            System.out.println("블록킹큐로 병렬 처리했을 때 걸린 시간 밀리초 : " + (end-start));
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
                    if(!taskBlockingQueue.isEmpty()){
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
                Thread.sleep(timeToNeed);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }//end of task

}//end of class
