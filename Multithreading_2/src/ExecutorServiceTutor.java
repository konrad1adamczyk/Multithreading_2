
/**

        Here you can see how ExecutorService is working.
        Replace ExecutorService.newSingleThreadExecutor() to newFixedThreadPool(). Notice how the time of work will change.
        Also experiment with number of threads in newFixedThreadPool().
        Try to change awaitTermination() parameters: try awaitTermination(1, TimeUnit.MILLISECONDS). Why currentThreads is not empty in this case?
        Also change the number of threads to 100 and try to execute.

**/



import java.util.*;
import java.util.concurrent.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutorServiceTutor {
    Object lock = new Object();
    Set<String> currentThreads = new HashSet<String>();

    static StringBuffer buf = new StringBuffer();
    static void log(String s) {
        buf.append(s+"\n");
    }

    public void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void logCurrentThreads() {
        StringBuilder currentThreadsStr = new StringBuilder();
        // create
        List<String> threads = new ArrayList<>(currentThreads);
        for (String str : threads) {
            currentThreadsStr.append(str).append(", ");
        }
        log("size:"+currentThreads.size()+
                ", current thread pool: "+currentThreadsStr.toString());
    }

    class TestThread implements Runnable {
        String threadName;

        public TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            /**
             * We add the thread to the list of executing threads
             * currentThreads, do some job (sleep to imitate it)
             * and then remove thread after finish.
             */
            currentThreads.add(threadName);
            logCurrentThreads();
            sleep(10);
            currentThreads.remove(threadName);
            logCurrentThreads();
        }
    }

    @Test
    public void testThread() {
        long start = new Date().getTime();

        List<Thread> threads = new ArrayList<Thread>();
        for (int i=0;i<100;i++) {
            threads.add(new Thread(new TestThread("t"+i)));
        }

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0;i<10;i++) {
            executorService.execute(threads.get(i));
        }

        executorService.shutdown();
        try {
//            executorService.awaitTermination(1, TimeUnit.SECONDS);
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();

        long time =new Date().getTime()-start;
        System.out.println("Time of work:"+time);
        System.out.println(buf);

        /**
         * There shouldn't be threads in currentThreads list after
         * successful finishing.
         */
        assertTrue(currentThreads.size()==0);

        /**
         * We should use multhithreading to improve perfomance
         */
        assertTrue(time<100);
    }

}
