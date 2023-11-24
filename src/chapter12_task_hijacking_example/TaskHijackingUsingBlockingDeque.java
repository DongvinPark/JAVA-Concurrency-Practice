package src.chapter12_task_hijacking_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

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
*/
public class TaskHijackingUsingBlockingDeque {
    public void run(){
        
    }//run()

    private final static class Worker implements Runnable {
        private final BlockingQueue<Task> taskBlockingQueue;
        private Worker(BlockingQueue<Task> taskBlockingQueue) {
            this.taskBlockingQueue = taskBlockingQueue;
        }
        @Override
        public void run() {
            for(int i=1; i<=1000; i++){

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
