package src.fill_array;

import java.time.LocalDateTime;

public class FillArrayByMultiThread {

  public void run() throws InterruptedException {

    int[] arr1 = new int[1_000_000_000];
    int[] arr2 = new int[1_000_000_000];

    // 램 16GB 짜리 게이밍 노트북에서는 배열 길이를 10억으로 주면 JVM이 이걸 감당하지를 못한다.
    // 따라서 1억으로 길이를 다시 수정했다.

    // 길이가 1억 인 배열에 0부터 9억 9999만 9999까지 채워 넣는다.

    // 처음엔 메인스레드에서 전부 처리한다.
    long start = System.currentTimeMillis();
    for (int i=0; i<arr1.length; i++){
      arr1[i] = i;
    }
    long end = System.currentTimeMillis();
    System.out.printf("One Main Thread Time millisecond : %d \n", (end - start));
    System.out.println("arr1 마지막 값 : " + arr1[arr1.length-1]);

    // 그 다음엔 스레드 2개 만들어서 동시에 작업하게 만든다.
    System.out.println("멀티 스레드 시작 시각 : " + LocalDateTime.now());
    for (int i=0; i<1000000000; i += 500_000_000) {
      //System.out.println("i = " + i);
      Thread t = new Thread(new FillArrayRunnableImpl(i, i + 499_999_999, arr2));
      t.start();
    }

    // 테스트 결과, 길이 10억인 배열을 스레드 1개가 채워 넣으나, 스레드 2개 또는 10개가 채워 넣으나
    // 후자가 전자보다 34 밀리초 정도 더 빠르다는 것 말고는 큰 차이가 없었다.
    // 스레드를 새로 만드는 것에 시간이 많이 들어가는 것도 아닌데, 동시에 두 스레드가 작업하는 것이
    // 한 스레드가 작업하는 것보다 시간이 그렇게 드라마틱하게 확 줄어들지는 않는다.
    // 이건 아무래도 배열이라는 자료구조가 동시성을 막는 어떤 락이 있는 것 같다.
  }

  static class FillArrayRunnableImpl implements Runnable {
    private final int startIdx;
    private final int endIdx;
    private final int[] arr;

    public FillArrayRunnableImpl(int startIdx, int endIdx, int[] arr){
      this.startIdx = startIdx;
      this.endIdx = endIdx;
      this.arr = arr;
    }

    public void run()
    {
      //System.out.println("run called!!"); 여기가 호출되는 건 확실하다!!
      for (int i = startIdx; i<= endIdx; i++){
        arr[i] = i;
      }
      if(endIdx == 999_999_999) {
        System.out.println("멀티 스레드 작업 종료 시각 : " + LocalDateTime.now());
        System.out.println("arr2 마지막 값 : " + arr[arr.length-1]);
      }
    }
  }

}//end of src.fill_array.FillArrayByMultiThread
