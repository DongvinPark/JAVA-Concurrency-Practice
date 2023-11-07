package src.fill_array;

import java.time.LocalDateTime;

public class FillArrayByMultiThread {

  public void run() throws InterruptedException {

    int[] arr1 = new int[100_000_000];
    int[] arr2 = new int[100_000_000];

    // 램 16GB 짜리 게이밍 노트북에서는 배열 길이를 10억으로 주면 JVM이 이걸 감당하지를 못한다.
    // 따라서 1억으로 길이를 다시 수정했다.

    // 길이가 1억 인 배열에 0부터 9999만 9999까지 채워 넣는다.

    // 처음엔 메인스레드에서 전부 처리한다.
    long start = System.currentTimeMillis();
    for (int i=0; i<arr1.length; i++){
      arr1[i] = i;
    }
    long end = System.currentTimeMillis();
    System.out.printf("One Main Thread Time millisecond : %d \n", (end - start));
    System.out.println("arr1 마지막 값 : " + arr1[arr1.length-1]);

    // 그 다음엔 스레드 100개 만들어서 동시처리 시킨다.
    // 스레드 1개당 1천 만 번 배열에 쓰기 작업해야 한다.
    // 둘 다 시간을 측정해서 출력한다.
    LocalDateTime startTime = LocalDateTime.now();
    System.out.println("멀티 스레드 시작 시각 : " + startTime);
    for (int i=0; i<100000000; i += 10_000_000) {
      //System.out.println("i = " + i);
      Thread t = new Thread(new RunnableImpl(i, i + 9_999_999, arr2));
      t.start();
    }

    System.out.println("arr2 마지막 요소 값 : " + arr2[arr2.length-1]);
    // 윈도우에서 실행했을 때는 배열길이 1억일 때, 스레드가 1개일 경우 32밀리초, 100개인 경우 8밀리초가 걸렸지만,
    // 동일 상황에서 우분투 리눅스는 스레드 1개일 때 32밀리초, 100개일 때 오히려 120밀리초가 걸리며 시간이 더 오래 걸렸다.
    // 이는 스레드 생성 자체가 시스템 리소스를 잡아먹기 때문에 이로 인해 오히려 속도가 더 느려졌음을 뜻한다.

    // 배열 길이를 10억으로 늘리고 멀티 스레드 개수를 10으로 줄이자, 스레드 1개일 때는 250밀리초가 걸렸던 것을
    // 스레드 10개일 때는 1~3 밀리초밖에 걸리지 않게 되었다.
    // 무조건 스레드를 많이 만드는 것이 능사는 아님을 보여주는 실험결과다.
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
      //System.out.println("run called!!"); 여기가 호출되는 건 확실하다!!
      for (int i = startIdx; i<= endIdx; i++){
        arr[i] = i;

        if(i== 99_999_999){
          System.out.println("멀티 스레드 작업 종료 시각 : " + LocalDateTime.now());
        }
      }
    }
  }

}//end of src.fill_array.FillArrayByMultiThread
