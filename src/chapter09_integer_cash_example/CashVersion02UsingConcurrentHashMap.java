package src.chapter09_integer_cash_example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

// 맵 자료구조에 10, 100, 1000개의 스레드가 1~100 까지의 숫자의 제곱 값을 랜덤순서로 동시에 요청한다.
// 맵에 숫자가 있다면 반환하고, 없다면 계산해서 맵에 등록(==캐시)해 놓는다.
// 개별 스레드는 자신이 요청한 값에 대한 캐시응답 값을 받아서 전부 더한다.
// 열 개 스레드가 자신이 계산한 결과를 미리 정의된 정답과 비교해서 답이 틀릴 때만 표준출력으로 보고 한다.
// 모든 스레드가 동시에 작업을 시작해서 응답을 마치기까지 걸린 시간을 측정한다.
// 측정 결과, 일반 해시맵을 synchronized 키워드로 단순 동기화한 경우와 ConcurrentHashMap을 쓴 경우에 대해서
// 스레드 개수가 10개일 때는 둘이 약 90만 나노초 정도로 거의 비슷했다.
// 그러나, 스레드 개수가 100개일 때는 전자가 440만 나노초, 후자가 400만 나노초가 측정되면서 후자가 10% 정도 더 빨랐다.
// 스레드 개수가 1천 개로 늘어나자, 전자는 3200만 나노초, 후자는 2200만 나노초로, 거의 30% 정도 더 빠른 성능을 보여줬다.
// 스레드 개수가 많아질수록, 그리고 1개 스레드가 작업을 처리하는 시간이 길어질수록, 단순 synchronized를 이용한
// 동기화는 성능 측면에서 부정적인 결과를 보여주었다.
public class CashVersion02UsingConcurrentHashMap {
  private static final int ANSWER = 338_350;// 1^2 + 2^2 + ... + 100^2
  private static final int NUMBER_OF_THREADS = 10;

  private final ConcurrentHashMap<Integer, Integer> cashMap;

  public CashVersion02UsingConcurrentHashMap(ConcurrentHashMap<Integer, Integer> cashMap) {
    this.cashMap = cashMap;
  }

  public void run(){

    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch endGate = new CountDownLatch(NUMBER_OF_THREADS);

    for(int i=1; i<= NUMBER_OF_THREADS; i++){
      Thread t = new Thread(
          new CashVersion02UsingConcurrentHashMap.TaskImpl(getNewRandumOrderList(), startGate, endGate)
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
    int max = Integer.MAX_VALUE;
    while(max != 0){max--;}
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
        if(result != ANSWER){
          System.out.println("Wrong...");
        }
      }
    }
  }// TaskImpl
}//end of class
