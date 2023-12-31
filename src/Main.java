package src;

import src.chapter14_interrupt_and_cancel_example.CancelBigIntegerProducerByInterrupt;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    /*FillArrayByMultiThread fillArrayByMultiThread
     = new FillArrayByMultiThread();
    fillArrayByMultiThread.run();*/

    /*FillMapByMultiThread fillMapByMultiThread
     = new FillMapByMultiThread();
    fillMapByMultiThread.run();*/

    /*FillMapByExecutorService fillMapByExecutorService
     = new FillMapByExecutorService();
    fillMapByExecutorService.run();*/

    /*VisibilityTest visibilityTest
     = new VisibilityTest();
    visibilityTest.run();*/

    /*SynchronizedQueueUsingMonitorLock synchronizedQueueUsingMonitorLock
     = new SynchronizedQueueUsingMonitorLock();
    synchronizedQueueUsingMonitorLock.run();*/

    /*GateExampleUsingCountDownLatch gateExampleUsingCountDownLatchInstance
     = new GateExampleUsingCountDownLatch();
    System.out.println(
            gateExampleUsingCountDownLatchInstance.timeTasks(10, new Runnable() {
              @Override
              public void run() {
                System.out.println("Task Started!!");
              }
            })
        + " nano second passed"
    );*/

    /*CheckTaskUsingFuture checkTaskUsingFuture
     = new CheckTaskUsingFuture();
      checkTaskUsingFuture.run();*/

    /*SquareCalculatorUsingRecursiveTask squareCalculatorUsingRecursiveTask
        = new SquareCalculatorUsingRecursiveTask();
    squareCalculatorUsingRecursiveTask.run();*/

    /*ThreadLocalTest threadLocalTest
            = new ThreadLocalTest();
    threadLocalTest.run();*/

    /*CheckTaskUsingCompletableFuture checkTaskUsingCompletableFuture
            = new CheckTaskUsingCompletableFuture();
    checkTaskUsingCompletableFuture.run();*/

    /*CashVersion01UsingSynchronizedKeyword cashVersion01UsingSynchronizedKeyword
            = new CashVersion01UsingSynchronizedKeyword(new HashMap<Integer, Integer>());
    cashVersion01UsingSynchronizedKeyword.run();

    CashVersion02UsingConcurrentHashMap cashVersion02UsingConcurrentHashMap
        = new CashVersion02UsingConcurrentHashMap(new ConcurrentHashMap<Integer, Integer>());
    cashVersion02UsingConcurrentHashMap.run();

    CashVersion03UsingFutureObject cashVersion03UsingFutureObject
            = new CashVersion03UsingFutureObject(new ConcurrentHashMap<Integer, FutureTask<Integer>>());
    cashVersion03UsingFutureObject.run();*/

    /*ReturnOrThrowThatIsTheProblem returnOrThrowThatIsTheProblem
        = new ReturnOrThrowThatIsTheProblem();
    returnOrThrowThatIsTheProblem.run();*/

    /*ProducersAndConsumersWithoutBlockingQueue producersAndConsumersWithoutBlockingQueue
        = new ProducersAndConsumersWithoutBlockingQueue();
    producersAndConsumersWithoutBlockingQueue.run();

    TaskHijackingUsingBlockingDeque taskHijackingUsingBlockingDeque
            = new TaskHijackingUsingBlockingDeque();BrokenPrimeProducer
    taskHijackingUsingBlockingDeque.run();*/

    /*FillBuffersVersion01WithSingleThread fillBuffersVersion01WithSingleThread
            = new FillBuffersVersion01WithSingleThread();
    fillBuffersVersion01WithSingleThread.run();

    FillBuffersVersion02WithCompletionService fillBuffersVersion02WithCompletionService
            = new FillBuffersVersion02WithCompletionService();
    fillBuffersVersion02WithCompletionService.run();

    FillBuffersVersion03WithFuture fillBuffersVersion03WithFuture
        = new FillBuffersVersion03WithFuture();
    fillBuffersVersion03WithFuture.run();*/

    /*FillBuffersVersion04WithExecutorService fillBuffersVersion04WithExecutorService
            = new FillBuffersVersion04WithExecutorService();
    fillBuffersVersion04WithExecutorService.run();*/

    /*FailedToCancelBigIntegerProducer failedToCancelBigIntegerProducer
            = new FailedToCancelBigIntegerProducer();
    failedToCancelBigIntegerProducer.run();*/

    CancelBigIntegerProducerByInterrupt cancelBigIntegerProducerByInterrupt
            = new CancelBigIntegerProducerByInterrupt();
    cancelBigIntegerProducerByInterrupt.run();
  }//main
}//main class