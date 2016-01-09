package BlochkinQueueProducerConsumer;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Konrad on 2016-01-09.
 */
public class Producer implements Runnable {
    protected BlockingQueue queue = null;
    public Producer(BlockingQueue queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try { for (int i =0; i<20; i++){
            queue.put(i);
            Thread.sleep(1000);
        }
//            queue.put("1");
//            Thread.sleep(1000);
//            queue.put("2");
//            Thread.sleep(1000);
//            queue.put("3");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
