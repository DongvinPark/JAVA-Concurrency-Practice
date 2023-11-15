package src.chapter06_future_example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CheckTaskUsingCompletableFuture {

    public void run(){

        /*
        Future는 해당 태스크를 실행해줄 스레드풀을 따로 만들어준 후,
        거기에 Future 태스크를 별도로 submit해줘야 했지만,
        CompletableFuture는 내부적으로 별도의 ForkJoinPool을 활용하기 때문에 이러한 작업이 필요 없으며,
        Future 를 실행시킨 스레드풀을 따로 종료시켜줄 필요도 없기 때문에 더 편리하게 사용할 수 있다.
        * */
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(5_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "CompletableFuture is Done!!";
                }
        );

        try {
            while(true){
                if(stringCompletableFuture.isDone()){
                    System.out.println("CompletableFuture result : " + stringCompletableFuture.get());
                    break;
                } else {
                    // 아래와 같이 루프 안에서 Thread.sleep을 호출하면, 해당 스레드는 일을 하는 것이 아님에도
                    // cpu 자원을 점유하고 있는다. 이러한 상황을 busy-waiting이라고 한다.
                    // 이번 예시에서는 Future 객체가 아직 완료되지 않았음을 보여주기 위해서 이렇게 코딩했지만,
                    // 실전에서는 이러한 busy-waiting 상태를 방치해서는 안 된다.
                    Thread.sleep(1000);
                    System.out.println("CompletableFuture task thread still in processing ... / status : " + stringCompletableFuture.isDone());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }//run()

}
