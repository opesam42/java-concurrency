import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AccountWithoutSync{
    private static Account account = new Account();
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args){
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i=0; i < 100; i++){
            executor.execute(new AddOnePenny());
        }

        executor.shutdown();

        while(!executor.isTerminated()){
            
        }
        
        System.out.println("What is my account balance? " + account.getBalance());
    }
        
    private static class AddOnePenny implements Runnable {
        @Override
        public void run(){
            account.deposit(1);
            System.out.println("A penny added!!!");
        }
    }

    private static class Account {
        private double balance = 0.0;

        public double getBalance(){
            return balance;
        }

        public void deposit(double amount){
            // lock the method
            lock.lock();
            
            double newBalance = balance + amount;

            // added to magnify the error 
            try{
                Thread.sleep(5);
            } 
            catch(InterruptedException ex){

            }

            balance = newBalance;

            lock.unlock();
        }
    }

}
