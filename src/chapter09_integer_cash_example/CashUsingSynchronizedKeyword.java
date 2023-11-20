package src.chapter09_integer_cash_example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// 맵 자료구조에 10개의 스레드가 1~100 까지의 숫자의 제곱 값을 랜덤순서로 동시에 요청한다.
// 맵에 숫자가 있다면 반환하고, 없다면 계산해서 맵에 등록(==캐시)해 놓는다.
// 개별 스레드는 자신이 요청한 값에 대한 캐시응답 값을 받아서 전부 더한다.
// 열 개 스레드가 자신이 응답 받은 값들을 전부 보고한다.
// 정답(1^2 + 2^2 + ... + 100^2 == 338_350)을 맞춘 스레드의 개수와 모든 스레드가 동시에 작업을 시작해서 응답을 마치기까지 걸린 시간을 측정한다.
public class CashUsingSynchronizedKeyword {

    private static final int ANSWER = 338_350;// 1^2 + 2^2 + ... + 100^2
    private static final int NUMBER_OF_THREADS = 10;

    private final HashMap<Integer, Integer> cashMap;

    public CashUsingSynchronizedKeyword(HashMap<Integer, Integer> cashMap) {
        this.cashMap = cashMap;
    }

    public void run(){

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(NUMBER_OF_THREADS);

        for(int i=1; i<= NUMBER_OF_THREADS; i++){
            Thread t = new Thread(
                    new TaskImpl(getNewRandumOrderList(), startGate, endGate)
            );
            t.start();
        }

        try {
            long start = System.nanoTime();
            startGate.countDown();
            endGate.await();
            long end = System.nanoTime();

            System.out.println("Elapsed time in nano seconds : " + (end-start));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }//run

    private static List<Integer> getNewRandumOrderList(){
        List<Integer> list = new ArrayList<>();
        for(int i =1; i<= 100; i++) list.add(i);
        Collections.shuffle(list);
        return list;
    }

    private synchronized Integer compute(int requestNumber){
        Integer computeResult;
        if(!cashMap.containsKey(requestNumber)){
            cashMap.put(requestNumber ,requestNumber*requestNumber);
        }
        computeResult = cashMap.get(requestNumber);
        if(computeResult == null) return 0;
        else return computeResult;
    }

    private final class TaskImpl implements Runnable {

        private final List<Integer> numberList;
        private final CountDownLatch startGate;
        private final CountDownLatch endGate;

        private int result = 0;

        private TaskImpl(
                List<Integer> numberList,
                CountDownLatch startGate,
                CountDownLatch endGate
        ) {
            this.numberList = numberList;
            this.startGate = startGate;
            this.endGate = endGate;
        }

        @Override
        public void run() {
            try {
                startGate.await();
                for(int num : numberList){
                    result += compute(num);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                endGate.countDown();
                System.out.println("Thread " + Thread.currentThread().getId() + " answer : " + result);
            }
        }
    }// TaskImpl

}//end of class
