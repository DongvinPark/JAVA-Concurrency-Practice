package src.chapter13_text_and_image_buffer_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
HTML 페이지를 표시할 때 사용할 텍스트 30 개와 이미지 30개를 각각의 버퍼에 채워 넣는 프로그램.

텍스트는 textArr 안에 미리 주어져 있지만,
이미지는 네트워크 I/O를 이용해서 다운로드 받아야하므로 텍스트보다 시간이 더 많이 걸린다.
500 밀리초 보다 오래 결릴 경우 이미지 다운로드를 중단해야 한다.

이미지를 다운로드 받는 것에 걸리는 시간은 10~1000 밀리초 중 랜덤하게 선택된다.

실행 결과, 모든 이미지 다운로드 작업이 전부 이후의 작업을 블록킹 해버리기 때문에 실행시간이
약 10초나 걸렸다.
*/
public class FillBuffersWithSingleThread {
    public void run(){
        String[] textArr = {
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        };

        BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
        BlockingQueue<ImageData> imageDataBuffer = new LinkedBlockingQueue<>();

        long start = System.currentTimeMillis();
        for(String s : textArr){
            stringBuffer.offer(s);
        }

        for(int i=1; i<=30; i++){
            imageDataBuffer.offer(new ImageData().downLoadImageData());
        }
        long end = System.currentTimeMillis();

        System.out.println("단일 스레드 실행 시간 밀리초 : " + (end-start));
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
