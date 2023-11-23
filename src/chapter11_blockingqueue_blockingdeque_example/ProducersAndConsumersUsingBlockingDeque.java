package src.chapter11_blockingqueue_blockingdeque_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
10 개의 프로듀서 스레드들이 각각 1000 개의 작업을 BlockingQueue에 집어 넣고,
이와 동시에 10개의 컨슈머 스레드들이 BlockingQueue의 작업들을 꺼내서 처리한다.

작업 한 개를 처리하는 것에 걸리는 시간은 1밀리초 ~ 10밀리초 이고, 이는 작업을 처리하기 시작한 시점에
ThreadLocalRandom 난수에 의해서 결정된다.

컨슈머 스레드는 각자의 BlockingDeque을 가지고 있으며 BlockingQueue에서 작업을 가져와서
각자의 BlockingDeque에 저장한 후 처리한다.
특정 컨슈머 스레드가 자신의 BlockingDeque이 텅 비게 된다면, 노는 것이 아니라 다른 컨슈머 스레드의 덱에서
작업을 빼와서 처리 한다.
*/
public class ProducersAndConsumersUsingBlockingDeque {
    public void run(){

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

    private final static class Task {
        private boolean isTaskDone = false;
        public void work(){
            try {
                int timeToNeed = ThreadLocalRandom.current().nextInt(1,11);
                Thread.sleep(timeToNeed);
                isTaskDone = true;
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

        public boolean getTaskDoneStatus(){
            return isTaskDone;
        }
    }//end of task

}//end of class
