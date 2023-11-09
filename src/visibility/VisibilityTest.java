package src.visibility;

public class VisibilityTest {
    private static volatile boolean ready;
    private static volatile int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run(){
            while(!ready){
                System.out.println("not yet!!");
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public void run() throws InterruptedException {
        new ReaderThread().start();

        Thread.sleep(1);

        number = 42;
        ready = true;
    }

}
