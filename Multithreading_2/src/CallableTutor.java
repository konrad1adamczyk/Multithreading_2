import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;

/**
 *Interface Callable allows to create threads that return the execution result T.
*
*
* 1) Try to run the class and look at the results and execution time. Replace the call of newSingleThreadExecutor () to newFixedThreadPool() and compare the execution time.
* 2) Instead of executorService.execute() use executorService.submit(). Save results in the array of Future objects.
* 3) Try to stop the execution of the first 3 streams using the method cancel(). Handle CancellationException.
* 4) Before finishing the thread add sleep for 150 seconds.
* Experiment with the size of the pool: how many threads will complete the work, and how many do not (set, for example, pool of 2).
* On termination of the thread, add logging, informing if the work was finished:
* log ("cancelling thread" + i + ", isDone =" + results.get(i).isDone());
* 5) Add a message after the completion of sleep of StringGenerator:
* log ("thread is finished:" + allStrings [index]);
* Try to experiment with the parameter of cancel() method: set true or false.
*
*/
public class CallableTutor {
    static StringBuffer buf = new StringBuffer();
    static void log(String s) {
        buf.append(s+"\n");
    }

    public class StringGenerator implements Callable<String> {

        public String call() throws Exception {
            String[] allStrings = { "Cow", "Goose", "Cat", "Dog",
                    "Elephant", "Rabbit", "Snake", "Chicken",
                    "Horse", "Human" };
            int index = (int)(Math.random()*100)/10;

            Thread.sleep(10);
            return allStrings[index];
        }
    }

    @Test
    public void testCallable() {
        long start = new Date().getTime();
        List<Thread> threads = new ArrayList<Thread>();

        ArrayList<Future<String>> results = new ArrayList<Future<String>>();

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i=0;i<10;i++) {
            results.add(executorService.submit(new StringGenerator()));
        }

        // try to cancel 5 of 10 threads here
        for (int i=0;i<5;i++) {
            log("cancelling thread "+i);
            results.get(i).cancel(true);
        }

        StringBuilder resultStr = new StringBuilder();
        for(Future<String> result: results){
            try {
                // The blocking get call
                resultStr.append(result.get());
                resultStr.append(" ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (CancellationException ce){
                ce.printStackTrace();
            }
        }
        System.out.println(resultStr);

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Thread.sleep(150);

        long time =new Date().getTime()-start;
        System.out.println("Time of work:"+time);

        System.out.println(buf);
    }

}
