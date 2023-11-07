package src.fill_array;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FillArrayByExecutorService {
  public void run() {
    ExecutorService executor = Executors.newFixedThreadPool(10);

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


    // 그 다음, executorService에 작업을 등록시켜서 실행해본다.
    // 스레드 10개이고, 1개가 1천 만 개씩 채워 넣는다.
    start = System.currentTimeMillis();
    List<Callable<String>> callableTaskList = new ArrayList<>();

    for(int i=0; i<100_000_000; i+= 10_000_000){
      callableTaskList.add(
          new CollableTask(i, i+9_999_999, arr2)
      );
    }

    try {
      executor.invokeAll(callableTaskList);
      end = System.currentTimeMillis();

      System.out.printf("10 Multi Thread Time millisecond : %d \n", (end - start));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }//end of run()

  private static class CollableTask implements Callable<String> {

    private final int startIdx;
    private final int endIdx;
    private final int[] arr;

    public CollableTask(int startIdx, int endIdx, int[] arr){
      this.startIdx = startIdx;
      this.endIdx = endIdx;
      this.arr = arr;
    }

    @Override
    public String call() {
      for (int i = startIdx; i<= endIdx; i+=2){
        arr[i] = i;
      }
      return "executor completed : thread id - " + Thread.currentThread().getId();
    }
  }

}//end of class