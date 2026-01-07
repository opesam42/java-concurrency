public class TaskThreadDemo_ThreadStop {
    public static void main(String[] args){
        // create tasks
        Runnable printA = new PrintChar('A', 100);
        Runnable printB = new PrintChar('B', 60);
        Runnable printZ = new PrintNum(50, 90);

        // create thread
        Thread thread1 = new Thread(printA);
        Thread thread2 = new Thread(printB);
        Thread thread3 = new Thread(printZ);

        // start thread
        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println("\n");
        
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
    private int numToPrint;
    private int times;

    public PrintNum(int n, int t){
        numToPrint = n;
        times = t;
    }

    @Override //overides the Runnable method run() 
    public void run(){
        try{
            for (int i=0; i < times; i++){
            System.out.print(numToPrint);
            Thread.sleep(500);
        }
        } catch(InterruptedException ex){
            System.out.println("\nThread " + numToPrint + " was interrupted and is stopping.");
        }
        
    }

}    
