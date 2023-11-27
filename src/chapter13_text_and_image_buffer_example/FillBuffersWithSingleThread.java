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
*/
public class FillBuffersWithSingleThread {
    public void run(){
        String[] textArr = {
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
                "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        };

        BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
        BlockingQueue<ImageData> imageDateBuffer = new LinkedBlockingQueue<>();

    }//run()

    private static final class ImageData {
        private ImageData(){}
        public ImageData downLoadImageData(){
            int waitTime = ThreadLocalRandom.current().nextInt(10, 1001);

            if (waitTime > 500) return null;
            try {
                Thread.sleep(waitTime);
                return new ImageData();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}//end of class
