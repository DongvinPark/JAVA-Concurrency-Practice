package src;

import src.fill_map.FillMapByExecutorService;
import src.fill_map.FillMapByMultiThread;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    /*FillArrayByMultiThread fillArrayByMultiThread = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();*/

    /*FillMapByMultiThread fillMapByMultiThread = new FillMapByMultiThread();
    fillMapByMultiThread.run();*/

    FillMapByExecutorService fillMapByExecutorService = new FillMapByExecutorService();
    fillMapByExecutorService.run();
  }//main
}//main class