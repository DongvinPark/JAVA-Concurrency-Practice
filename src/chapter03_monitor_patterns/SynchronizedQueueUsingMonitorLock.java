package src.chapter03_monitor_patterns;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class SynchronizedQueueUsingMonitorLock {
    public void run(){
        // 모니터 락, 또는 암묵적 락으로 불리는 synchronized 키워드를 사용하여
        // 일반 컬렉션에 10개의 스레드가 값을 채워 넣을 수 있게 만들었다.

        MonitorLockQueue monitorLockQueue = new MonitorLockQueue(new LinkedList<>());

        System.out.println("시작 시각 : " + LocalDateTime.now());
        for(int i=1; i<=10; i++){
            Thread t = new Thread(new TaskImplForMonitorQueue(monitorLockQueue));
            t.start();
        }
    }

    private static class TaskImplForMonitorQueue implements Runnable {
        private final MonitorLockQueue queue;

        public TaskImplForMonitorQueue(MonitorLockQueue queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i=0; i<10_000; i++) {
                queue.add(i);
            }
            if(queue.size() == 100_000) {
                System.out.println("완료 시각 : " + LocalDateTime.now());
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