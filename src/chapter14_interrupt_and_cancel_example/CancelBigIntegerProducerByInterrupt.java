package src.chapter14_interrupt_and_cancel_example;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
새롭게 만든 스레드가 용량 10짜리 BlockingQueue에 BigInteger를 마구 집어 넣고, 메인 스레드는
10밀리초 간  슬립하다가 앞서 만든 스레드한게 인터럽트를 건다.

인터럽트를 걸지 않았다면, 이 프로그램은 BlockingQueue의 put 메서드의 특성으로 인해
누군가 BlockingQueue에서 내용물을 빼가지 않는 한 영원히 wait 상태로 멈춰 있게 된다.

다행히 인터럽트가 들어와서 작업은 제때 멈출 수 있게 된다.
* */
public class CancelBigIntegerProducerByInterrupt {
    public void run(){
        try {
            BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>(10);

            BrokenBigIntegerProducerThread forThread = new BrokenBigIntegerProducerThread(queue);

            Thread thread = new Thread(forThread);
            long start = System.nanoTime();
            thread.start();

            Thread.sleep(10);
            System.out.println("캔슬 호출 직전!!");
            thread.interrupt();
            long end = System.nanoTime();
            System.out.println("스레드 시작부터 인터럽트 호출 완료까지 걸린 시간 나노초 : " + (end-start));
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }//run()

    static class BrokenBigIntegerProducerThread implements Runnable {
        private final BlockingQueue<BigInteger> queue;

        BrokenBigIntegerProducerThread(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while(!Thread.currentThread().isInterrupted()){
                    p = getNextNumber(p);
                    queue.put( p );
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private BigInteger getNextNumber(BigInteger input){
            return input.add(BigInteger.ONE);
        }
    }//end of BrokenPrimeProducer
}//end of class
