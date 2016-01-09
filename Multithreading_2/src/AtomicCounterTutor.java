
/**
        Add a counter that counts the number of calls.
        Why counter shows different values (please rerun if jUnit passed successfully)?
        How to fix this?

        Please use AtomicInteger instead of int for counter variable.
        Use method getAndIncrement() to increment counter.
**/


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.*;

import org.junit.Test;

public class AtomicCounterTutor {
    final int ITERATIONS = 1000000;
    int counter=0;

    class TestThread implements Runnable {
        String threadName;

        public TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i=0;i<ITERATIONS;i++) {
                counter++;
            }
        }
    }

    @Test
    public void testThread() {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i=0;i<100;i++) {
            threads.add(new Thread(new TestThread("t"+i)));
        }
        System.out.println("Starting threads");
        for (int i=0;i<100;i++) {
            threads.get(i).start();
        }
        try {
            for (int i=0;i<100;i++) {
                threads.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter="+counter);

        assertTrue(counter==ITERATIONS*100);
    }

}
