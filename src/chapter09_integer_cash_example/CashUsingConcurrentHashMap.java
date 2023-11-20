package src.chapter09_integer_cash_example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class CashUsingConcurrentHashMap {
  private static final int ANSWER = 338_350;// 1^2 + 2^2 + ... + 100^2
  private static final int NUMBER_OF_THREADS = 1000;

  private final ConcurrentHashMap<Integer, Integer> cashMap;

  public CashUsingConcurrentHashMap(ConcurrentHashMap<Integer, Integer> cashMap) {
    this.cashMap = cashMap;
  }

  public void run(){

    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch endGate = new CountDownLatch(NUMBER_OF_THREADS);

    for(int i=1; i<= NUMBER_OF_THREADS; i++){
      Thread t = new Thread(
          new CashUsingConcurrentHashMap.TaskImpl(getNewRandumOrderList(), startGate, endGate)
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
        if(result != ANSWER){
          System.out.println("Wrong...");
        }
      }
    }
  }// TaskImpl
}//end of class
