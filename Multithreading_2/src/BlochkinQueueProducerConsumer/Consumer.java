package BlochkinQueueProducerConsumer;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Konrad on 2016-01-09.
 */
public class Consumer implements Runnable {
    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            for (int i =0; i<20; i++){
                System.out.println(queue.take());
            }
//            System.out.println(queue.take());
//            System.out.println(queue.take());
//            System.out.println(queue.take());
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
