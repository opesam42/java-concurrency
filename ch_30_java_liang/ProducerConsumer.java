import java.util.concurrent.*;

public class ProducerConsumer {

    private static ArrayBlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(3);
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ProducerTask());
        executor.execute(new ConsumerTask());

        executor.shutdown();
    }
 

    private static class ProducerTask implements Runnable {
        @Override
        public void run(){
            try{
                int i = 1;
                while(true){
                    System.out.println("Producer writes " + i);
                    buffer.put(i++);

                    // put thread to sleep
                    Thread.sleep((int)(Math.random() * 10000));
                }
            } catch(InterruptedException ex){

            }
        }
    }


    private static class ConsumerTask implements Runnable {
        @Override
        public void run(){
            try{
                while(true){
                    // take the head of  the queue, as it uses FIFO
                    int int_read = buffer.take();
                    System.out.println("The consumer reads " + int_read);
                     Thread.sleep((int)(Math.random() * 10000));
                }
            } catch(InterruptedException ex) {}
        }
    }
}