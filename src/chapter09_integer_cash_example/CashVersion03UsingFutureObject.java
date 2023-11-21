package src.chapter09_integer_cash_example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/*
세 번째 테스트는 캐시용으로 사용하는 ConcurrentHashMap에 단순 정수타입이 아니라, FutureTask<Integer> 타입을 넣고,
캐시해 놓을 값을 계산할 때, 정수 맥스 값이 0이 될 때까지 -= 1 계산을 하게 만들어서 고의적으로 오랜 시간이 걸리게 했다.

이러한 상황에서 10개의 스레드가 동시에 캐시에게 1~100의 정수의 제곱 값을 요청하게 만들고, 요청시 값이 없으면 계산해서 맵에 집어넣게 했다.
테스트 결과,
단순 synchronized를 사용한 경우와, ConcurrentHashMap<Integer, Integer>를 사용한 경우는 둘다 약 5.1~5.4 밀리초로 측정 되었지만,
ConcurrentHashMap<Integer, FutureTask<Integer>>를 사용한 경우엔 이들보다 약 4배 빠른 1.38 밀리초로 측정되었다.

FutureTask를 쓸 경우, 두 개의 스레드가 거의 동시에 같은 키 값으로 맵에 get 요청 했을 때 먼저 요청한 스레드가 FutureTask를 집어넣었는데
아주 약간 나중에 요청한 스레드가 또다시 FutureTask를 집어넣지 않게 해줘서 두 번 해야할 연산을 한 번으로 줄일 수 있다.
단순 ConcurrentHashMap만을 사용한 경우, 이때 시간이 오래 드는 연산이 두 번 일어나게 되면서 실행시간을 느리게 만든다.

연산을 실행하는 데 시간이 오래 걸리지 않는다면, 앞의 두 경우보다 FutureTask를 쓴 쪽이 오히려 시간이 synchronized를 쓴 경우보다
약 5~10% 느린 것으로 측정되지만,
멀티 스레드 환경에서 여러 스레드들에 의해서 호출되는 연산이 실행하는 것에 아주 약간이라도 시간이 더 걸리면 FutureTask를 쓰는 것이
성능을 획기적으로 향상시키는 것으로 측정되었다.
* */
public class CashVersion03UsingFutureObject {
    private static final int ANSWER = 338_350;// 1^2 + 2^2 + ... + 100^2
    private static final int NUMBER_OF_THREADS = 10;

    private final ConcurrentHashMap<Integer, FutureTask<Integer>> cashMap;

    public CashVersion03UsingFutureObject(ConcurrentHashMap<Integer, FutureTask<Integer>> cashMap) {
        this.cashMap = cashMap;
    }

    public void run(){

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(NUMBER_OF_THREADS);

        for(int i=1; i<= NUMBER_OF_THREADS; i++){
            Thread t = new Thread(
                    new CashVersion03UsingFutureObject.TaskImpl(getNewRandumOrderList(), startGate, endGate)
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
        try {
            FutureTask<Integer> computeResult;
            if(!cashMap.containsKey(requestNumber)){
                FutureTask<Integer> futureTask = new FutureTask<>(
                        ()->{
                            int max = Integer.MAX_VALUE;
                            while(max != 0){max--;}
                            return requestNumber*requestNumber;
                        }
                );
                futureTask.run();
                cashMap.put(requestNumber ,futureTask);
            }
            computeResult = cashMap.get(requestNumber);
            if(computeResult.isDone()){
                return computeResult.get();
            } else {
                while(true){
                    if(computeResult.isDone()){
                        return computeResult.get();
                    }
                    System.out.println("future task not done!!");
                }
            }
        } catch (CancellationException e) {
            cashMap.remove(requestNumber);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return 0;
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
                if(result != ANSWER){
                    System.out.println("Wrong...");
                }
            }
        }
    }// TaskImpl
}
