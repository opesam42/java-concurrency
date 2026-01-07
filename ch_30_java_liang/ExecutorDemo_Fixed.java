// create a thread pool

import java.util.concurrent.*;

public class ExecutorDemo_Fixed{
    public static void main(String[] args){
        // create a fixed thread pool of 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);
        

        // submit tasks to the executor
        executor.execute(new PrintChar('d', 100));
        executor.execute(new PrintChar('e', 68));
        executor.execute(new PrintNum(45, 100));

        // shutdown the executor
        executor.shutdown();
    }
}


class PrintChar implements Runnable{
    private char charToPrint;
    private int times;

    public PrintChar(char c, int t){
        charToPrint = c;
        times = t;
    }

    @Override //overides the Runnable method run() 
    public void run(){
        for (int i=0; i < times; i++){
            System.out.print(charToPrint);
        }
    }
}


class PrintNum implements Runnable{
    private int start_num;
    private int end_num;

    public PrintNum(int s, int e){
        start_num = s;
        end_num = e;
    }

    @Override //overides the Runnable method run() 
    public void run(){
        for (int i=start_num; i < end_num+1; i++){
            System.out.print(" " + i + " ");
        }
    }
}    
