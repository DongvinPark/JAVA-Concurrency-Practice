package src.fill_map;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FillMapByExecutorService {
  public void run(){
    // 이번엔 해시맵에 값을 채워 넣는 것을 스레드를 직접 생성하는 것이 아니라,
    // ExecutorService를 이용해서 구현해보자.

    HashMap<Integer, Integer> commonMap = new HashMap<>();
    ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    long start = System.currentTimeMillis();
    for(int i=0; i< 10_000_000; i++){
      commonMap.put(i,i);
    }
    long end = System.currentTimeMillis();
    System.out.println("1개 스레드 실행시간 : " + (end-start));

    ExecutorService executor = Executors.newFixedThreadPool(10);

    System.out.println("멀티스레딩 작업 시작 시각 : " + LocalDateTime.now());
    for(int i=0; i<10_000_000; i+=1_000_000){
      executor.execute(
          new TaskImpl(i, i + 999_999, concurrentHashMap)
      );
    }

    executor.shutdown();
  }//run()

  private static class TaskImpl implements Runnable {
    private final int startIdx;
    private final int endIdx;
    private final ConcurrentHashMap<Integer, Integer> concurrentHashMap;

    private TaskImpl(
        int startIdx, int endIdx, ConcurrentHashMap<Integer, Integer> concurrentHashMap
    ) {
      this.startIdx = startIdx;
      this.endIdx = endIdx;
      this.concurrentHashMap = concurrentHashMap;
    }

    @Override
    public void run() {
      for(int i=startIdx; i<=endIdx; i++){
        concurrentHashMap.put(i,i);
      }
      if(concurrentHashMap.size() == 10_000_000){
        System.out.println("멀티스레딩 작업 완료 시각 : " + LocalDateTime.now());
      }
    }
  }

}
