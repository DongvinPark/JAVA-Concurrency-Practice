package src;

import src.fill_map.FillMapByMultiThread;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    /*FillArrayByMultiThread fillArrayByMultiThread = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();*/

    FillMapByMultiThread fillMapByMultiThread = new FillMapByMultiThread();
    fillMapByMultiThread.run();
  }//main
}//main class