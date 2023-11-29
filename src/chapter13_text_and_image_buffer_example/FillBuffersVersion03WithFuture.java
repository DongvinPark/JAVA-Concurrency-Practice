package src.chapter13_text_and_image_buffer_example;

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

단, 이번에는 Future 클래스에서 제공하는 get(long timeout, Timeunit unit){...} 메서드를 활용해서,
퓨처 객체가 작업을 끝내기를 기다릴 수 있는 최대 시간을 내가 일일이 컨트롤 할 필요없이 간편하게 설정할 수 있었다.

테스트를 하면서 한 가지 깨달은 점은, Future 타입의 객체들은 Executor 프레임웍과 같이 쓰기가 어렵다는 것이다.
*/
public class FillBuffersVersion03WithFuture {
  public void run() throws InterruptedException {
    String[] textArr = {
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
    };

    BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
    BlockingQueue<byte[]> imageDataBuffer = new LinkedBlockingQueue<>();

    BlockingQueue<Future<byte[]>> futureBlockingQueue = new LinkedBlockingQueue<>();

    for(String s : textArr){
      stringBuffer.offer(s);
    }

    long start = System.currentTimeMillis();
    long end;

    for(int i=1; i<=30; i++){
      futureBlockingQueue.offer(
          CompletableFuture.supplyAsync(
              () -> {
                try {
                  int waitTime = ThreadLocalRandom.current().nextInt(10, 1001);
                  Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
                return new byte[]{'i', 'm', 'a', 'g', 'e', 'd', 'a', 't', 'a'};
              }
          )
      );
    }

    for(int i=1; i<=30; i++){
      Thread thread = new Thread(new TaskImpl(futureBlockingQueue, imageDataBuffer));
      thread.start();
    }

    while(true){
      if(imageDataBuffer.size() == 30){
        end = System.currentTimeMillis();
        /*for(int i=1; i<=30; i++){
          System.out.println("arr = " + Arrays.toString(imageDataBuffer.poll()));
        }*/
        break;
      }
    }
    System.out.println("퓨처를 이용한 멀티스레드 실행 시간 밀리초 : " + (end-start));
  }//run()

  private static final class TaskImpl implements Runnable {
    private final BlockingQueue<Future<byte[]>> futureBlockingQueue;
    private final BlockingQueue<byte[]> destBlockingQueue;

    public TaskImpl(
        BlockingQueue<Future<byte[]>> futureBlockingQueue,
        BlockingQueue<byte[]> destBlockingQueue
    ) {
      this.futureBlockingQueue = futureBlockingQueue;
      this.destBlockingQueue = destBlockingQueue;
    }

    @Override
    public void run() {
      try {
        while(true){
          if(!futureBlockingQueue.isEmpty()){
            Future<byte[]> imageDataFuture = futureBlockingQueue.poll();
            destBlockingQueue.offer(imageDataFuture.get(500, TimeUnit.MILLISECONDS));
            //System.out.println("다운로드 성공!!");
          } else break;
        }
      } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        } catch (TimeoutException e) {
          //System.out.println("다운로드 실패 - 타임아웃!!");
          destBlockingQueue.offer(new byte[]{});
        }
    }

  }// end of TaskImpe class

}// end of class
