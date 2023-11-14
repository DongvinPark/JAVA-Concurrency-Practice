package src.chapter05_latch_example;

import java.util.concurrent.CountDownLatch;

public class GateExampleUsingCountDownLatch {
    public long timeTasks (int numberOfThreadsToMake, final Runnable inputTask) throws InterruptedException {

        // 스레드 여러 개를 순차적으로 만들어나갈 때, 1번 스레드 만들기 & 실행 >> 2번 스레드 만들기 & 실행 >> ...
        // 이런 식으로 만드는 것과 실행하는 것을 번갈아가면서 진행하는 것이 아니라,
        // 1번 스레드 만들기 ... N 번 스레드 만들기 >> 1번 ~ N번 스레드 동시에 실행의 순서로 진행하기 위해서 startGate가 필요하다.
        // startGate가 생성될 때 입력되 1이라는 카운트가 0으로 감소했을 때 N 개의 스레드가 동시에 실행된다.
        final CountDownLatch startGate = new CountDownLatch(1);

        final CountDownLatch endGate = new CountDownLatch(numberOfThreadsToMake);

        // startGate가 0이 되기 전에 미리 N 개의 스레드를 만들어 둔다.
        for (int i=0; i<numberOfThreadsToMake; i++){
            Thread t = new Thread(() -> {
                try {
                    // 이 부분이 바로, startGate의 카운트가 0이 되기를 기다리는 부분이다.
                    startGate.await();
                    try {
                        inputTask.run();
                    } finally {
                        // 각각의 스레드는 자기 할 일을 마친 후, endGate의 카운트를 1 만큼 감소시킨다.
                        endGate.countDown();
                    }
                } catch (InterruptedException interruptedException) {
                    System.out.println("InterruptedException occurred in Thread id : " + Thread.currentThread().getId());
                    interruptedException.printStackTrace();
                }
            });
            t.start();
        }//for

        long start = System.nanoTime();
        // startGate의 카운트를 1에서 0으로 만들어서, 앞서 만들어둔 N 개이 스레드들이 동시에 실행될 수 있게 한다.
        startGate.countDown();

        // N 개의 스레드들에 의해서 endGate의 카운트가 0으로 변경되기를 기다린다.
        endGate.await();
        long end = System.nanoTime();

        // N 개의 스레드가 실행을 완료하기 까지 걸린 나노초 단위 시간을 리턴한다.
        return end-start;
    }// timeTasks()
}// end of class
