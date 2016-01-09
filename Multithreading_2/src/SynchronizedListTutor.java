/**
 *Why does the program (periodically) fall with ArrayIndexOutOfBoundException?
 What should I do to prevent this from happening?
 (An exception is thrown not always, might need to run the program multiple times.)
 */

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SynchronizedListTutor {
    static boolean failed = false;
    static StringBuffer buf = new StringBuffer();
    static void log(String s) {
        buf.append(s+"\n");
    }
    static void err(String s) {
        buf.append("<span style='color:red'><b>"+s+"</b></span>\n");
        failed = true;
    }

    static String[] animals = { "Cow", "Goose", "Cat", "Dog",
            "Elephant", "Rabbit", "Snake", "Chicken",
            "Horse", "Human" };

//    List<String> randomAnimals = new ArrayList<String>();
        List<String> randomAnimals = Collections.synchronizedList(new ArrayList<String>()); //**************************

    public String getRandomAnimal() {
        int index = (int)(Math.random()*animals.length);
        return animals[index];
    }

    class TestThread implements Runnable {
        String threadName;

        public TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                for (int i=0;i<50000;i++) {
                    randomAnimals.add(getRandomAnimal());
//                    print(new ArrayList(randomAnimals));  // nowa linia
                }
            } catch(Exception e) {
                err(e.getClass().getName());
            }
        }
    }

    public void print(Collection<?> c) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = c.iterator();
        while(iterator.hasNext()) {
            builder.append(iterator.next());
            builder.append(" ");
        }
        log(builder.toString());
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
        System.out.println("Waiting for threads");
        try {
            for (int i=0;i<100;i++) {
                threads.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(buf);
        assertFalse(failed);
    }


}
