package src.chapter13_text_and_image_buffer_example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FillBuffersWithFuture {
  public void run(){
    String[] textArr = {
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
        "text", "text", "text", "text", "text", "text", "text", "text", "text", "text",
    };

    BlockingQueue<String> stringBuffer = new LinkedBlockingQueue<>();
    BlockingQueue<byte[]> imageDataBuffer = new LinkedBlockingQueue<>();

    BlockingQueue<Future<byte[]>> futureBlockingQueue = new LinkedBlockingQueue<>();

    long start = System.currentTimeMillis();
    for(String s : textArr){
      stringBuffer.offer(s);
    }

    for(int i=1; i<=30; i++){
      futureBlockingQueue.offer(
          CompletableFuture.supplyAsync(
              () -> {
                try {
                  int waitTime = ThreadLocalRandom.current().nextInt(10, 1001);
                  //System.out.println("waitTime = " + waitTime);
                  //System.out.println(Thread.currentThread().getId());
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
    long end = System.currentTimeMillis();

    System.out.println("퓨처를 이용한 멀티스레드 실행 시간 밀리초 : " + (end-start));
    for(int i=1; i<=1; i++){
      System.out.println("arr = " + imageDataBuffer.size());
    }
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
      if(!futureBlockingQueue.isEmpty()){
        //System.out.println("이프 진입");
        try {
          Future<byte[]> imageDataFuture = futureBlockingQueue.poll();
          while(true){
            if(imageDataFuture.isDone()){
              destBlockingQueue.offer(imageDataFuture.get());
              System.out.println("삽입!!");
              break;
            } else {
              Thread.sleep(100);
            }
          }
        } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
