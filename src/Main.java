package src;

import src.latch_example.TestHarness;

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

    /*CompareMonitorLockAndThreadSafeObject compareMonitorLockAndThreadSafeObject = new CompareMonitorLockAndThreadSafeObject();
    compareMonitorLockAndThreadSafeObject.run();*/
    TestHarness testHarnessInstance = new TestHarness();
    System.out.println(
            testHarnessInstance.timeTasks(10, new Runnable() {
              @Override
              public void run() {
                System.out.println("작업 시작!!");
              }
            })
    );
  }//main
}//main class