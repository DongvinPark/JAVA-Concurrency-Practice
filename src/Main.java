package src;

import src.chapter06_future_example.CheckTaskUsingFuture;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    /*FillArrayByMultiThread fillArrayByMultiThread = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();*/

    /*FillMapByMultiThread fillMapByMultiThread = new FillMapByMultiThread();
    fillMapByMultiThread.run();*/

    /*FillMapByExecutorService fillMapByExecutorService = new FillMapByExecutorService();
    fillMapByExecutorService.run();*/

    /*VisibilityTest visibilityTest = new VisibilityTest();
    visibilityTest.run();*/

    /*SynchronizedQueueUsingMonitorLock synchronizedQueueUsingMonitorLock = new SynchronizedQueueUsingMonitorLock();
    synchronizedQueueUsingMonitorLock.run();*/

    /*GateExampleUsingCountDownLatch gateExampleUsingCountDownLatchInstance = new GateExampleUsingCountDownLatch();
    System.out.println(
            gateExampleUsingCountDownLatchInstance.timeTasks(10, new Runnable() {
              @Override
              public void run() {
                System.out.println("Task Started!!");
              }
            })
        + " nano second passed"
    );*/

      CheckTaskUsingFuture checkTaskUsingFuture = new CheckTaskUsingFuture();
      checkTaskUsingFuture.run();
  }//main
}//main class