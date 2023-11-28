package src.chapter13_text_and_image_buffer_example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
HTML 페이지를 표시할 때 사용할 텍스트 30 개와 이미지 30개를 각각의 버퍼에 채워 넣는 프로그램.

텍스트는 textArr 안에 미리 주어져 있지만,
이미지는 네트워크 I/O를 이용해서 다운로드 받아야하므로 텍스트보다 시간이 더 많이 걸린다.
500 밀리초 보다 오래 결릴 경우 이미지 다운로드를 중단해야 한다.

이미지를 다운로드 받는 것에 걸리는 시간은 10~1000 밀리초 중 랜덤하게 선택된다.
*/
public class FillBuffersWithCompletionService {
    public void run(){

        String[] textArr = {
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        };

        BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
        BlockingQueue<ImageData> imageDateBuffer = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(30);

        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);

        List<ImageData> imageDataList = new ArrayList<>();
        for(int i=1; i<=30; i++){
            imageDataList.add(new ImageData());
        }

        for(ImageData imageData : imageDataList){
            /*
            아래의 코드는 다음의 메서드 레퍼런스를 이용해서  completionService.submit(imageData::downLoadImageData);
             라는 1 줄의 코드로 줄일 수 있지만,
            Callable을 사용하고 있다는 것을 분명히 표시하기 위해서 일부러 오버라이드가 남아 있는 코드를 사용하였다.
            */
            completionService.submit(
                    new Callable<>() {
                        @Override
                        public ImageData call() throws Exception {
                            return imageData.downLoadImageData();
                        }
                    }
            );
        }

        try {
            long start = System.currentTimeMillis();
            for(String s : textArr){
                stringBuffer.offer(s);
            }

            for(int i=1; i<=30; i++){
                imageDateBuffer.offer(completionService.take().get());
            }
            long end = System.currentTimeMillis();
            System.out.println("CompletionService 실행시간 밀리초 : " + (end-start));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }

    }//run()

    private static final class ImageData {
        private byte[] data = null;
        private ImageData(){}
        public ImageData downLoadImageData(){
            int waitTime = ThreadLocalRandom.current().nextInt(10, 1001);
            try {
                if (waitTime > 500) {
                    Thread.sleep(500);
                    System.out.println("이미지 다운로드 실패!!");
                    return new ImageData();
                } else {
                    Thread.sleep(waitTime);
                    data = new byte[]{'i', 'm', 'a', 'g', 'e', 'd', 'a', 't', 'a'};
                    return new ImageData();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}//end of class
