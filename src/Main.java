package src;

import src.monitor_patterns.CompareMonitorLockAndThreadSafeObject;

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

    CompareMonitorLockAndThreadSafeObject compareMonitorLockAndThreadSafeObject = new CompareMonitorLockAndThreadSafeObject();
    compareMonitorLockAndThreadSafeObject.run();
  }//main
}//main class