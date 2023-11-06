public class FillArrayByMultiThread {

  public void run(){
    System.out.println("Hello World!!");

    int[] arr1 = new int[100_000_000];
    int[] arr2 = new int[100_000_000];

    // 길이가 1억 인 배열에 0부터 9999만 9999까지 채워 넣는다.

    // 처음엔 메인스레드에서 전부 처리한다.
    long start = System.currentTimeMillis();
    for (int i=0; i<arr1.length; i++){
      arr1[i] = i;
    }
    long end = System.currentTimeMillis();
    System.out.printf("One Main Thread Time millisecond : %d \n", (end - start));

    // 그 다음엔 스레드 100개 만들어서 동시처리 시킨다.
    // 스레드 1개당 1백만 번 배열에 쓰기 작업해야 한다.
    start = System.currentTimeMillis();
    for (int i=0; i<100; i++) {
      Thread t = new Thread(new RunnableImpl(i, i + 999_999, arr2));
      t.start();
    }
    end = System.currentTimeMillis();

    System.out.printf("100 Multi Thread Time millisecond : %d \n", (end - start));

    // 둘 다 시간을 측정해서 출력해보았다.
  }

  static class RunnableImpl implements Runnable {
    private final int startIdx;
    private final int endIdx;
    private final int[] arr;

    public RunnableImpl(int startIdx, int endIdx, int[] arr){
      this.startIdx = startIdx;
      this.endIdx = endIdx;
      this.arr = arr;
    }

    public void run()
    {
      for (int i = startIdx; i<= endIdx; i++){
        arr[i] = i;
      }
    }
  }

}//end of FillArrayByMultiThread
