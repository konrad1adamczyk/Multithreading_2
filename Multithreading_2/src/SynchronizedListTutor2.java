/**
 * Here the problem from SynchronizedListTutor is solved (by using of Collections.synchronizedList).
 However, here we're collecting all list items into StringBuffer in print() method.
 Why ConcurrentModificationException is thrown?
 That's happens because the list is changing in run() while we're iterating over it in print().
 Please fix this problem by using CopyOnWriteArrayList. It will create a new instance of the list for each adding/removing of element.
 Then the list instance passed to print() method will not be changed.
 */



import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Use CopyOnWriteArrayList
 */
public class SynchronizedListTutor2 {
    static StringBuffer buf = new StringBuffer();
    static boolean failed = false;
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

    List<String> randomAnimals =
            Collections.synchronizedList(new ArrayList<String>());

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
                for (int i=0;i<20;i++) {
                    randomAnimals.add(getRandomAnimal());
                    print(randomAnimals);
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
        // You can uncomment this if you would like to see lists
        //log(builder.toString());
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
        System.out.println("Success!");
    }


}
