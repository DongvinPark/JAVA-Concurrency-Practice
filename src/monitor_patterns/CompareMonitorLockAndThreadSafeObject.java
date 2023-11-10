package src.monitor_patterns;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CompareMonitorLockAndThreadSafeObject {
    public void run(){
        // 동일한 조건에서 세 가지 상황을 테스트하고 실행시간을 측정한다.
        // 1. Not Thread Safe한 큐에 10개 스레드가 각각 10_000 번씩 푸시
        // 2. 처음부터 스레드 세이프하게 설계된 컬렉션에 푸시
        // 3. 모니터 락이 적용된 일반 큐에 푸시

        /*
        * 테스트 결과, 첫 번째 경우는 데드락에 걸렸는지 전혀 완료 표시가 뜨지 않았고,
        * 두 번째 경우는 세 번째 경우보다 약 4~7 밀리초 정도 느렸다.
        * 두 번째 경우와 세 번재 경우는 약 27 ~ 34 밀리초의 시간이 걸렸다.
        * */

        Queue<Integer> notThreadSafeQueue = new LinkedList<>();
        LinkedBlockingQueue<Integer> threadSafeDesignedQueue = new LinkedBlockingQueue<>();
        MonitorLockQueue monitorLockQueue = new MonitorLockQueue(new LinkedList<>());

        executeTest(notThreadSafeQueue, "첫 번째");

        executeTest(threadSafeDesignedQueue, "두 번째");

        System.out.println("실행 시각 " + "세 번째" + " : " + LocalDateTime.now());
        for(int i=1; i<=10; i++){
            Thread t = new Thread(new TaskImplForMonitorQueue(monitorLockQueue, "세 번째"));
            t.start();
        }
    }

    private void executeTest(Queue<Integer> queue, String suffix){
        System.out.println("실행 시각 " + suffix + " : " + LocalDateTime.now());
        for(int i=1; i<=10; i++){
            Thread thread = new Thread(new TaskImplForQueue(queue, suffix));
            thread.start();
        }
    }

    private static class TaskImplForQueue implements Runnable {
        private final Queue<Integer> queue;
        private final String suffix;

        public TaskImplForQueue(Queue<Integer> queue, String suffix){
            this.queue = queue;
            this.suffix = suffix;
        }

        @Override
        public void run() {
            for (int i=0; i<10_000; i++) {
                queue.add(i);
            }
            if(queue.size() == 100_000) {
                System.out.println("완료 시각 " + suffix + " : " + LocalDateTime.now());
            }
        }
    }

    private static class TaskImplForMonitorQueue implements Runnable {
        private final MonitorLockQueue queue;
        private final String suffix;

        public TaskImplForMonitorQueue(MonitorLockQueue queue, String suffix){
            this.queue = queue;
            this.suffix = suffix;
        }

        @Override
        public void run() {
            for (int i=0; i<10_000; i++) {
                queue.add(i);
            }
            if(queue.size() == 100_000) {
                System.out.println("완료 시각 " + suffix + " : " + LocalDateTime.now());
            }
        }
    }

}

class MonitorLockQueue {
    private final Object myLock = new Object();

    private final Queue<Integer> queue;

    public MonitorLockQueue(Queue<Integer> queue){
        this.queue = queue;
    }

    public int size() {
        return queue.size();
    }

    public void add(Integer number){
        synchronized (myLock){
            queue.add(number);
        }
    }
}