package src.chapter15_interrupt_and_cancel_example;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
BigInteger를 용량 10자리 블록킹큐에다가 집어넣다가, 메인스레드에서 10 밀리초 후에 착업을 취소시키는 예제다.

BlockingQueue 인터페이스에는 큐에 요소를 집어넣는 것에 사용할 수 있는 3가지 메서드가 있다. put, add, offer다.

put은 큐에다가 내용물을 집어넣을 수 있을 때까지 현재 스레드를 wait() 상태로 만들어서 대기시킨다.

add 는 큐가 가득 차서 내용물을 넣을 수 없는 경우, 집어넣을 수 있을 때까지 대기하지 않고 즉시 IllegalStateException을 발생시킨다.

offer 는 큐에 내용물을 넣을 수 없는 경우, exception을 발생시키지 않고, 그저 즉시 false를 리턴한다.

>>> put의 경우 현재 작업을 취소하는 기준이 되는 cancelled 플래그를 스레드가 wait 상태일 경우 체크를 할 수가 없게 된다.
이 경우엔 cancelled 같은 플래그에 의존하는 작업취소 정책이 적절하지 않다.
* */
public class FailedToCancelBigIntegerProducer {
    public void run(){
        try {
            BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>(10);

            BrokenBigIntegerProducerThread forThread = new BrokenBigIntegerProducerThread(queue);

            Thread thread = new Thread(forThread);
            thread.start();

            Thread.sleep(10);
            System.out.println("캔슬 호출 직전!!");
            forThread.cancel();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }//run()

    static class BrokenBigIntegerProducerThread implements Runnable {

        private final BlockingQueue<BigInteger> queue;
        private volatile boolean cancelled = false;

        BrokenBigIntegerProducerThread(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while(!cancelled){
                    System.out.println("큐 insert 성공 ? : " + queue.offer( getNextNumber(p) ));
                    /*queue.put( getNextNumber(p) );
                    System.out.println("큐 insert 성공");*/
                }
            } catch (IllegalStateException e) {
                System.out.println("Broken Producer 내부 큐 가득 참!!!!");
            }/* catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }

        public void cancel(){
            cancelled = true;
        }

        private BigInteger getNextNumber(BigInteger input){
            return input.add(BigInteger.ONE);
        }
    }//end of BrokenPrimeProducer

}//end of class
