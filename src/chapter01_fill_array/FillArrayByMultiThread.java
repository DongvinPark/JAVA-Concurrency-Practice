package src.chapter01_fill_array;

import java.util.concurrent.CountDownLatch;

public class FillArrayByMultiThread {

  public void run() throws InterruptedException {

    int[] arr1 = new int[200_000_000];
    int[] arr2 = new int[200_000_000];

    // 길이가 2억 인 배열에 0부터 1억 9999만 9999까지 채워 넣는다.

    // 처음엔 메인스레드에서 전부 처리한다.
    long start = System.currentTimeMillis();
    for (int i=0; i<arr1.length; i++){
      arr1[i] = i;
    }
    long end = System.currentTimeMillis();
    System.out.printf("One Main Thread Time millisecond : %d \n", (end - start));
    System.out.println("arr1 마지막 값 : " + arr1[arr1.length-1]);

    // 그 다음엔 스레드 2개 만들어서 동시에 작업하게 만든다.
    // CountdownLatch를 도입해서 10개의 스레드들이 따로따로 작업을 시작하는 것이 아니라, 일제히 작업을
    // 시작할 수 있게 만든다.
    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch endGate = new CountDownLatch(20);

    for (int i=0; i<200_000_000; i += 10_000_000) {
      //System.out.println("i = " + i);
      Thread t = new Thread(
          new FillArrayRunnableImpl(
              i, i + 9_999_999, arr2
              ,startGate, endGate
          )
      );
      t.start();
    }

    start = System.currentTimeMillis();
    startGate.countDown();
    endGate.await();
    end = System.currentTimeMillis();
    System.out.printf("10 Multi Thread Time millisecond : %d \n", (end - start));
    System.out.println("arr2 마지막 값 : " + arr2[arr2.length-1]);

    // 카운트 다운 래치를 이용해서 20개의 스레드가 일제히 작업을 진행하게 만들자,
    // 1개 스레드로는 73밀리초가 걸린 일을 거의 2배의 속도인 40밀리초만에 끈낸 것을 확인할 수 있었다.
  }

  static class FillArrayRunnableImpl implements Runnable {
    private final int startIdx;
    private final int endIdx;
    private final int[] arr;
    private final CountDownLatch startGate;
    private final CountDownLatch endGate;

    public FillArrayRunnableImpl(
        int startIdx, int endIdx, int[] arr,
        CountDownLatch startGate,
        CountDownLatch endGate
    ){
      this.startIdx = startIdx;
      this.endIdx = endIdx;
      this.arr = arr;
      this.startGate = startGate;
      this.endGate = endGate;
    }

    public void run()
    {
      try {
        startGate.await();
        try {
          //System.out.println("run called!!"); 여기가 호출되는 건 확실하다!!
          for (int i = startIdx; i<= endIdx; i++){
            arr[i] = i;
          }
        } finally {
          endGate.countDown();
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }//run()
  }

}//end of src.fill_array.FillArrayByMultiThread
