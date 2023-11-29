package src.chapter13_text_and_image_buffer_example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/*
HTML 페이지를 표시할 때 사용할 텍스트 30 개와 이미지 30개를 각각의 버퍼에 채워 넣는 프로그램.

텍스트는 textArr 안에 미리 주어져 있지만,
이미지는 네트워크 I/O를 이용해서 다운로드 받아야하므로 텍스트보다 시간이 더 많이 걸린다.
500 밀리초 보다 오래 결릴 경우 이미지 다운로드를 중단해야 한다.

이미지를 다운로드 받는 것에 걸리는 시간은 10~1000 밀리초 중 랜덤하게 선택된다.

30개의 스레드가 이미지를 개별로 다운로드 받고, 이미지 다운로드 시간을 500밀리초 이상을 기다리지 않기 때문에 이번에도
500~510 밀리초 정도가 측정되었다.
싱글 스레드로 처리한 경우(== 약 10,000 밀리초)보다 약 20배 정도 빨랐다.

이번 것이 코드가 가장 깔끔하다. 단, timeout이 나는 상황을 내가 if문을 이용해서 명시적으로 컨트롤해줘야 한다는 점이
조금 귀찮을 수 있다.

그리고, ExecutorService의 invokeAll(Callable 컬렉션, 타임아웃, 타임 단위) 메서드의 경우, 해당 ExecutorService
내의 스레드 중 단 1개라도 TimeoutException이 나서 착업이 취소될 경우, 나머지 모든 작업들도 전부 취소돼 버린다는 단점이 있다.
이러한 상태를 막기 위해서 Callable<T> 내에서 작업을 규정할 때, 500밀리초를 초과하더라도 예외를 던지는 것이 아니라
그냥 빈 빈 배열을 리턴하게 만들었고, 500 밀리초를 살짝 초과하더라도 ExecutorService 내의 모든 작업이 취솓되지 않도록,
spareTime을 더해서 invokeAll 메서드를 호출했다.
*/
public class FillBuffersVersion04WithExecutorService {
    public void run(){
        ExecutorService executorService = Executors.newFixedThreadPool(30);

        try {
            long start = System.currentTimeMillis();
            long end;
            List<Callable<byte[]>> callableList = new ArrayList<>();
            for(int i=1; i<=30; i++){
                callableList.add(
                        new Callable<byte[]>() {
                            @Override
                            public byte[] call() throws Exception {
                                int waitTime = ThreadLocalRandom.current().nextInt(10, 1001);
                                byte[] imageData;
                                if(waitTime > 500) {
                                    waitTime = 500;
                                    imageData = new byte[]{};
                                } else {
                                    imageData = new byte[]{'i', 'm', 'a', 'g', 'e', 'd', 'a', 't', 'a'};
                                }
                                Thread.sleep(waitTime);
                                return imageData;
                            }
                        }
                );
            }

            long spareTime = 10L;

            List<Future<byte[]>> resultFutureList = executorService.invokeAll(
                    callableList, 500 + spareTime, TimeUnit.MILLISECONDS
            );

            while(true){
                if(resultFutureList.size() == 30){
                    end = System.currentTimeMillis();
                    for(Future<byte[]> future : resultFutureList){
                        if(future.isDone()) {
                            System.out.println("future.get() = " + Arrays.toString(future.get()));
                        } else {
                            System.out.println("future not Done!!");
                        }
                    }
                    System.out.println("ExecutorService 멀티스레드 실행시간 밀리초 : " + (end-start));
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }


    }//run()
}//end of class
