package src.chapter07_fork_join_example;

import java.util.concurrent.RecursiveTask;

public class SquareCalculatorUsingRecursiveTask {
  public void run(){
    FactorialSquareCalculator factorialSquareCalculator
        = new FactorialSquareCalculator(10);

    int result = factorialSquareCalculator.compute();
    System.out.println("result = " + result);
  }
}

class FactorialSquareCalculator extends RecursiveTask<Integer> {

  private final Integer n;

  public FactorialSquareCalculator(Integer n){
    this.n = n;
  }

  @Override
  protected Integer compute() {
    if(n <= 1) return n;

    FactorialSquareCalculator calculator = new FactorialSquareCalculator(n-1);

    calculator.fork();

    return (n * n) + calculator.join();
  }
}
