package src.chapter11_blockingqueue_blockingdeque_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
10 개의 프로듀서 스레드들이 각각 1000 개의 작업을 BlockingQueue에 집어 넣고,
이와 동시에 10개의 컨슈머 스레드들이 BlockingQueue의 작업들을 꺼내서 처리한다.

작업 한 개를 처리하는 것에 걸리는 시간은 1밀리초 ~ 10밀리초 이고, 이는 작업을 처리하기 시작한 시점에
ThreadLocalRandom 난수에 의해서 결정된다.

컨슈머 스레드들을 자기가 가져온 작업을 처리하기만 할 뿐, 다른 컨슈머 스레드들의 작업 처리에는 관여하지 않는다.
*/
public class ProducersAndConsumersWithoutBlockingDeque {
    public void run(){
        BlockingQueue<Task> taskBlockingQueue = new LinkedBlockingQueue<>();

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
