package src.chapter08_threadlocal_example;

public class ThreadLocalTest {
    public void run(){
        ThreadLocal<Long> threadLocal = new ThreadLocal<>();

        // 하나의 ThreadLocal 객체에 2 개의 스레드가 각자 0, 1을 등록했다.
        // 각자의 스레드에서 똑같이 threadLocal.get()을 호출해도, 다른 스레드가 등록한 값에는 접근하지 못해서
        // 스레드 한정이 이루어지고 있는 것을 확인할 수 있다.
        for(long i=0; i<=1; i++){
            Thread t = new Thread(
                    new TaskImpl(threadLocal, i)
            );
            t.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }// run

    private static class TaskImpl implements Runnable {

        private final ThreadLocal<Long> threadLocal;
        private final long id;

        private TaskImpl(ThreadLocal<Long> threadLocal, long id) {
            this.threadLocal = threadLocal;
            this.id = id;
        }

        @Override
        public void run() {
            threadLocal.set(id);

            System.out.println("current id : " + id + " / threadLocal get() result : " + threadLocal.get());

        }
    }

}//end of class
