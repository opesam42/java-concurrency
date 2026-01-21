import java.util.concurrent.*;

public class ArrayBlockingQueueSample{
    
    
    public static void main(String[] args)
    throws InterruptedException {
        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(3);

        q.put(1);
        q.put(2);
        q.put(3);

        System.out.println("The queue is " + q + "\n");

        // take() takes the first element in the head of the queue using FIFO
        int first_int = q.take();
        System.out.println("The first number is " + first_int + "\n");

        System.out.println("The new queue is " + q);

    }
    
    
    
}