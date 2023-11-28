package src.chapter13_text_and_image_buffer_example;

import java.util.concurrent.*;

/*
HTML 페이지를 표시할 때 사용할 텍스트 30 개와 이미지 30개를 각각의 버퍼에 채워 넣는 프로그램.

텍스트는 textArr 안에 미리 주어져 있지만,
이미지는 네트워크 I/O를 이용해서 다운로드 받아야하므로 텍스트보다 시간이 더 많이 걸린다.
500 밀리초 보다 오래 결릴 경우 이미지 다운로드를 중단해야 한다.

이미지를 다운로드 받는 것에 걸리는 시간은 10~1000 밀리초 중 랜덤하게 선택된다.

30개의 스레드가 이미지를 개별로 다운로드 받고, 이미지 다운로드 시간을 500밀리초 이상을 기다리지 않기 때문에
500밀리초 정도가 측정되었다.
싱글 스레드로 처리한 경우(== 약 10,000 밀리초)보다 약 20배 정도 빨랐다.
*/
public class FillBuffersWithCompletionService {
    public void run(){

        String[] textArr = {
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        };

        BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
        BlockingQueue<byte[]> imageDateBuffer = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(30);

        CompletionService<byte[]> completionService = new ExecutorCompletionService<>(executor);

        long start = 0L;
        long end = 0L;

        try {
            start = System.currentTimeMillis();
            for(int i=1; i<=30; i++){
            /*
            아래의 코드는 다음의 람다를 이용해서 completionService.submit(() -> new ImageData().downLoadData());
             라는 1 줄의 코드로 줄일 수 있지만,
            Callable을 사용하고 있다는 것을 분명히 표시하기 위해서 일부러 오버라이드가 남아 있는 코드를 사용하였다.
            */
                completionService.submit(
                        new Callable<>() {
                            @Override
                            public byte[] call() {
                                return new ImageData().downLoadData();
                            }
                        }
                );
            }

            for(String s : textArr){
                stringBuffer.offer(s);
            }

            for(int i=1; i<=30; i++){
                imageDateBuffer.offer(completionService.take().get());
            }
            end = System.currentTimeMillis();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("CompletionService 실행 시간 밀리초 : " + (end-start));
            executor.shutdown();
        }

    }//run()

    private static final class ImageData {
        private byte[] data;
        public ImageData(){
            data = new byte[]{};
        }

        public byte[] downLoadData() {
            //System.out.println("다운로드데이터 호출!!");
            int threadSleepTime = ThreadLocalRandom.current().nextInt(10,1001);
            try {
                if(threadSleepTime > 500) {
                    System.out.println("이미지 다운로드 실패!!");
                    threadSleepTime = 500;
                } else {
                    data = new byte[]{'i', 'm', 'a', 'g', 'e', 'd', 'a', 't', 'a'};
                }
                Thread.sleep(threadSleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return data;
        }
    }

}//end of class
