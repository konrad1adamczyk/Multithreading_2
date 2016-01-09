/**
 * Here the problem from SynchronizedListTutor is solved (by using of Collections.synchronizedList).
 However, here we're collecting all list items into StringBuffer in print() method.
 Why ConcurrentModificationException is thrown?
 That's happens because the list is changing in run() while we're iterating over it in print().

 What to do to prevent this from happening?
 - Make a print () synchronized - does not help
 - Place the add () and print() in synchronized block - helps, but slow down
 - Use CopyOnWriteArrayList - helps
 - Pass Collections.unmodifiableList() to method print() - does not help
 - Pass copy of ArrayList to print() method - helps
 Please solve this problem by passing a copy of list to print() - you should use constructor of ArrayList.
 Then print() will be working with its own copy of list, which will not be changing.
 */



import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Use passing a copy of list to print()
 */
public class SynchronizedListTutor1 {
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
                for (int i=0;i<20;i++) {
                    randomAnimals.add(getRandomAnimal());
                    print(new ArrayList(randomAnimals));
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
    }


}
