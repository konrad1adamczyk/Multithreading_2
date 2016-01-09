
/**
 * Note that reading operation is slow (with sleep - it imitate the access to the slow resource).
 * Use ReentrantReadWriteLock in place of ReentrantLock, passing lock.writeLock() and lock.readLock() to Writing and Reading Threads.
 * How execution time has changed?
 * How the number of written Strings (len=...) has changed?
 * It increased because WritingThreads were able to work while ReadingThreads was sleeping.
 */




import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;

import org.junit.Test;


public class ReadWriteLockTutor {
    Thread t1, t2, t3, t4;
    Object monitor = new Object();
    int runningThreadNumber = 1;
    StringBuilder stringBuilder = new StringBuilder("");
    public static long ITERATIONS = 1000;

    static StringBuffer buf = new StringBuffer();
    static void log(String s) {
        buf.append(s+"\n");
    }

    class WritingThread implements Runnable {
        String threadName;
        Lock lock;

        public WritingThread(String threadName, Lock lock) {
            this.threadName = threadName;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                lock.lock();
                stringBuilder.append(threadName);
                Thread.yield();
                stringBuilder.append(threadName);
                Thread.yield();
                stringBuilder.append(",");
                lock.unlock();
                Thread.yield();
            }
        }
    }

    class ReadingThread implements Runnable {
        String threadName;
        Lock lock;

        public ReadingThread(String threadName, Lock lock) {
            this.threadName = threadName;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                //log(threadName+" is locked");
                String s = stringBuilder.toString();
                int len = s.length();
                int l = len>51?len-51:0;
                log(threadName+":len="+len+":"+s.substring(l));
                Thread.yield();
                //log(threadName+" is unlocked");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.yield();
                lock.unlock();
            }
        }
    }

    @Test
    public void testThread() {
        long start = new Date().getTime();

        /**
         * Use ReentrantReadWriteLock as a class for lock,
         * acquire different locks for ReadingThread and WritingThread
         * using lock.readLock() and lock.writeLock()
         */
        ReentrantLock lock = new ReentrantLock();
        t1 = new Thread(new WritingThread("1", lock));
        t2 = new Thread(new WritingThread("2", lock));
        t3 = new Thread(new ReadingThread("r1", lock));
        t4 = new Thread(new ReadingThread("r2", lock));

        System.out.println("Starting threads");
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        System.out.println("Waiting for threads");
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time =new Date().getTime()-start;
        log("Time of work:"+time);

        System.out.println(buf);
        assertTrue("Use ReentrantReadWriteLock", time<1000);
    }

}
