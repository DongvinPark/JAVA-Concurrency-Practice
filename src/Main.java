package src;

import src.chapter09_integer_cash_example.CashVersion02UsingConcurrentHashMap;
import src.chapter09_integer_cash_example.CashVersion03UsingFutureObject;
import src.chapter09_integer_cash_example.CashVersion01UsingSynchronizedKeyword;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import src.chapter10_callable_example.ReturnOrThrowThatIsTheProblem;
import src.chapter11_blockingqueue_blockingdeque_example.ProducersAndConsumersWithoutBlockingDeque;

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

    /*CashVersion01UsingSynchronizedKeyword cashVersion01UsingSynchronizedKeyword
            = new CashVersion01UsingSynchronizedKeyword(new HashMap<Integer, Integer>());
    cashVersion01UsingSynchronizedKeyword.run();

    CashVersion02UsingConcurrentHashMap cashVersion02UsingConcurrentHashMap
        = new CashVersion02UsingConcurrentHashMap(new ConcurrentHashMap<Integer, Integer>());
    cashVersion02UsingConcurrentHashMap.run();

    CashVersion03UsingFutureObject cashVersion03UsingFutureObject
            = new CashVersion03UsingFutureObject(new ConcurrentHashMap<Integer, FutureTask<Integer>>());
    cashVersion03UsingFutureObject.run();*/

    /*ReturnOrThrowThatIsTheProblem returnOrThrowThatIsTheProblem
        = new ReturnOrThrowThatIsTheProblem();
    returnOrThrowThatIsTheProblem.run();*/

    ProducersAndConsumersWithoutBlockingDeque producersAndConsumersWithoutBlockingDeque
        = new ProducersAndConsumersWithoutBlockingDeque();
    producersAndConsumersWithoutBlockingDeque.run();
  }//main
}//main class