package src;

import java.util.concurrent.ConcurrentHashMap;
import src.chapter09_integer_cash_example.CashUsingConcurrentHashMap;
import src.chapter09_integer_cash_example.CashUsingSynchronizedKeyword;

import java.util.HashMap;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    /*FillArrayByMultiThread fillArrayByMultiThread
     = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();*/

    /*FillMapByMultiThread fillMapByMultiThread
     = new FillMapByMultiThread();
    fillMapByMultiThread.run();*/

    /*FillMapByExecutorService fillMapByExecutorService
     = new FillMapByExecutorService();
    fillMapByExecutorService.run();*/

    /*VisibilityTest visibilityTest
     = new VisibilityTest();
    visibilityTest.run();*/

    /*SynchronizedQueueUsingMonitorLock synchronizedQueueUsingMonitorLock
     = new SynchronizedQueueUsingMonitorLock();
    synchronizedQueueUsingMonitorLock.run();*/

    /*GateExampleUsingCountDownLatch gateExampleUsingCountDownLatchInstance
     = new GateExampleUsingCountDownLatch();
    System.out.println(
            gateExampleUsingCountDownLatchInstance.timeTasks(10, new Runnable() {
              @Override
              public void run() {
                System.out.println("Task Started!!");
              }
            })
        + " nano second passed"
    );*/

    /*CheckTaskUsingFuture checkTaskUsingFuture
     = new CheckTaskUsingFuture();
      checkTaskUsingFuture.run();*/

    /*SquareCalculatorUsingRecursiveTask squareCalculatorUsingRecursiveTask
        = new SquareCalculatorUsingRecursiveTask();
    squareCalculatorUsingRecursiveTask.run();*/

    /*ThreadLocalTest threadLocalTest
            = new ThreadLocalTest();
    threadLocalTest.run();*/

    /*CheckTaskUsingCompletableFuture checkTaskUsingCompletableFuture
            = new CheckTaskUsingCompletableFuture();
    checkTaskUsingCompletableFuture.run();*/

    CashUsingSynchronizedKeyword cashUsingSynchronizedKeyword
            = new CashUsingSynchronizedKeyword(new HashMap<Integer, Integer>());
    cashUsingSynchronizedKeyword.run();

    CashUsingConcurrentHashMap cashUsingConcurrentHashMap
        = new CashUsingConcurrentHashMap(new ConcurrentHashMap<Integer, Integer>());
    cashUsingConcurrentHashMap.run();
  }//main
}//main class