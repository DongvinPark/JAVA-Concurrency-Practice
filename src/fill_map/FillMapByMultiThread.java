package src.fill_map;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FillMapByMultiThread {
    public void run(){
        // 이번엔 일만 해시맵에 키&값 쌍을 1천만 개 채워 넣는 것과
        // CoucurrentHashMap에 키&값 쌍을 멀티스레드로 1천만 개 채워 넣는 것을 비교해보자.

        // 테스트 결과, 속도 면에서는 ConcurrentHashMap이 일반 해시맵보다 이점이 없었지만,
        // 멀티스레드 환경에서 일반 스레드는 데드락이 발생하며 먹통이 된 방면, ConcurrentHashMap은
        // 일반 해시맵과 비교해서 거의 비슷한 성능을 내주는 것을 확인할 수 있었다.

        HashMap<Integer, Integer> commonMap = new HashMap<>();
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        long start = System.currentTimeMillis();
        for(int i=0; i< 10_000_000; i++){
            commonMap.put(i,i);
        }
        long end = System.currentTimeMillis();
        System.out.println("1개 스레드 실행시간 : " + (end-start));


        System.out.println("멀티스레드 시작 시각 : " + LocalDateTime.now());
        for(int i=0; i<10_000_000; i+= 1000000){
            Thread thread = new Thread(new FillMapRunnableImpl(i, i+999_999, concurrentHashMap));
            thread.start();
        }

    }//run()

    private static class FillMapRunnableImpl implements Runnable {
        private final int startIdx;
        private final int endIdx;
        private final ConcurrentHashMap<Integer,Integer> HashMap;

        public FillMapRunnableImpl(
                int startIdx, int endIdx, ConcurrentHashMap<Integer, Integer> map
        ){
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.HashMap = map;
        }

        @Override
        public void run() {
            for(int i = startIdx; i <= endIdx; i++){
                HashMap.put(i,i);
            }
            if(HashMap.size() == 10_000_000){
                System.out.println("멀티스레드 종료 시각 : " + LocalDateTime.now());
            }
        }
    }

}//end of class
