package src;

import src.latch_example.CountDownLatchUsingAsGateExample;

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

    CountDownLatchUsingAsGateExample countDownLatchUsingAsGateExampleInstance = new CountDownLatchUsingAsGateExample();
    System.out.println(
            countDownLatchUsingAsGateExampleInstance.timeTasks(10, new Runnable() {
              @Override
              public void run() {
                System.out.println("작업 시작!!");
              }
            })
        + " 나노초 걸림."
    );
  }//main
}//main class