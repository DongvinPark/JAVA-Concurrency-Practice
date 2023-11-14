package src.chapter06_future_example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckTaskUsingFuture {

    public void run() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<String> stringFuture = executorService.submit(
                    () -> {
                        Thread.sleep(5_000); // 5 sec
                        return "Future Completed!!";
                    }
            );

            while (true){
                if(stringFuture.isDone()){
                    System.out.println("Future result : " + stringFuture.get());
                    break;
                } else {
                    // 아래와 같이 루프 안에서 Thread.sleep을 호출하면, 해당 스레드는 일을 하는 것이 아님에도
                    // cpu 자원을 점유하고 있는다. 이러한 상황을 busy-waiting이라고 한다.
                    // 이번 예시에서는 Future 객체가 아직 완료되지 않았음을 보여주기 위해서 이렇게 코딩했지만,
                    // 실전에서는 이러한 busy-waiting 상태를 방치해서는 안 된다.
                    Thread.sleep(1000);
                    System.out.println("Future task thread still in processing ... / status : " + stringFuture.isDone());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdownNow();
        }
    }

}//end of class
