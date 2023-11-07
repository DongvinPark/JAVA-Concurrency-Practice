package src;

import src.fill_array.FillArrayByExecutorService;
import src.fill_array.FillArrayByMultiThread;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    FillArrayByMultiThread fillArrayByMultiThread = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();

    /*FillArrayByExecutorService fillArrayByExecutorService = new FillArrayByExecutorService();
    fillArrayByExecutorService.run();*/
  }//main
}//main class