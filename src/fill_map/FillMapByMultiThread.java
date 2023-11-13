package src.fill_map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class FillMapByMultiThread {
    public void run() throws InterruptedException {
        // 이번엔 일만 해시맵에 키&값 쌍을 1천만 개 채워 넣는 것과
        // CoucurrentHashMap에 키&값 쌍을 멀티스레드로 1천만 개 채워 넣는 것을 비교해보자.

        // 테스트 결과, 속도 면에서는 ConcurrentHashMap이 일반 해시맵보다 이점이 없었지만,
        // 멀티스레드 환경에서 일반 스레드는 데드락이 발생하며 먹통이 되었다.

        // 싱글 스레드로 1천 만 개 키/값 쌍을 채워 넣는 것은 일관적으로 470~480여 밀리초가 측정되었다.

        // Collections.synchronizedMap(new HashMap<>())을 이용해서 5개 스레드가 동시에 작업 시작하게
        // 만들자, 489밀리초 vs 1072밀리초가 측정되면서 후자가 전자보다 두 배 이상 느리게 작동했다.

        // CoucurrentHashMap을 이용하자 실행상황에 따라서 성능이 오락가락 했다. 어쩔 때는 1개 스레드로
        // 실행한 것보다 30~40 밀리초 정도 더 빨랐지만, 또 어쩔 때는 800 밀리초가 측정되면서 약 두 배 정도 느렸다.
        // 그래도 여러 번 실행해보면 대체로 싱글스레드로 실행한 것보다 약 40밀리초 정도만 더 걸리는 모습을 보여주면서
        // 멀티스레드 환경에서의 성능을 입증하였다.

        HashMap<Integer, Integer> commonMap = new HashMap<>();
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();


        long start = System.currentTimeMillis();
        for(int i=0; i< 10_000_000; i++){
            commonMap.put(i,i);
        }
        long end = System.currentTimeMillis();
        System.out.println("1개 스레드 실행시간 : " + (end-start));


        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(5);

        for(int i=0; i<10_000_000; i+= 2_000_000){
            Thread thread = new Thread(
                new FillMapRunnableImpl(
                    i, i+1_999_999, concurrentHashMap,
                    startGate, endGate
                )
            );
            thread.start();
        }

        start = System.currentTimeMillis();
        startGate.countDown();
        endGate.await();
        end = System.currentTimeMillis();
        System.out.println("10개 스레드 실행시간 : " + (end-start));
        System.out.println("concurrentHashMap Last Val = " + concurrentHashMap.get(9_999_999));
    }//run()

    private static class FillMapRunnableImpl implements Runnable {
        private final int startIdx;
        private final int endIdx;
        private final Map<Integer,Integer> HashMap;
        private final CountDownLatch startGate;
        private final CountDownLatch endGate;

        public FillMapRunnableImpl(
            int startIdx, int endIdx, Map<Integer, Integer> map,
            CountDownLatch startGate, CountDownLatch endGate
        ){
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.HashMap = map;
            this.startGate = startGate;
            this.endGate = endGate;
        }

        @Override
        public void run() {
            try {
                startGate.await();
                try {
                    for(int i = startIdx; i <= endIdx; i++){
                        HashMap.put(i,i);
                    }
                } finally {
                    endGate.countDown();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }//run()
    }

}//end of class
