package src.chapter10_callable_example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class ReturnOrThrowThatIsTheProblem {
  public void run(){
    /*
    10개의 스레드가 1~100 중 하나의 랜덤 난수를 생성하고, 그것이 짝수일 경우
    정상적으로 방금 발생시킨 난수를 리턴하고,
    홀수일 경우 예외를 던진다.

    예외가 던져졌을 경우, 캐치해서 결과 리스트에 null을 집어넣는다.

    Runnable을 implements 했을 경우 오버라이드해야 하는 메서드인 run()이
    리턴타입이 void라서 불편할 때가 있다.
    이때 Callable<V>를 implements해서 call() 메서드를 구현하면 값을 리턴할 수 있고,
    상황에 따라서 예외를 던지는 것도 가능하다.
    * */

    List<Integer> threadWorkResultList = new ArrayList<>();

    for (int i=1; i<=10; i++){
      CallableImpl callable = new CallableImpl();

      Integer result;
      try {
        result = callable.call();
        threadWorkResultList.add(result);
      } catch (Exception e) {
        threadWorkResultList.add(null);
      }
    }//for

    for (Integer result : threadWorkResultList) {
      if (result != null) {
        System.out.println("success : " + result);
      } else {
        System.out.println("\texception!!");
      }
    }
  }//run()

  private static final class CallableImpl implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
      int randomInteger = ThreadLocalRandom.current().nextInt(1,101);

      if(randomInteger % 2 == 0) return randomInteger;
      else throw new Exception("홀수라서 예외 발생!!");
    }//call
  }

}//end of class
