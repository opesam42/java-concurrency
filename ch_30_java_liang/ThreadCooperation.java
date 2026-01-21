import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreadCooperation{

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new DepositTask());
        executor.execute(new WithdrawTask());
        executor.shutdown();

    }

    private static Account account = new Account();
    
    public static class DepositTask implements Runnable{
        @Override
        public void run(){
            try{
                while (true) {
                    account.deposit((int)(Math.random()*10 + 1));
                    Thread.sleep(1000);
                }
            } catch(InterruptedException ex){

            }
        }
    }

    public static class WithdrawTask implements Runnable{
        @Override
        public void run(){
            
            while (true) {
                account.withdraw((int)(Math.random()*10 + 1));
            }
        }
    }

    public static class Account{
        private static Lock lock = new ReentrantLock();
        private static Condition newDeposit = lock.newCondition();

        private int balance = 0;

        public int getBalance(){
            return balance;
        }

        public void withdraw(int amount){
            lock.lock();

            try{
                while (balance < amount){
                    System.out.println("\t\t Waiting for deposits");
                    newDeposit.await();
                } 
                balance -= amount;
                System.out.println("Withdraw " + amount + "\t\t\t\t" + getBalance());
                
            } catch(InterruptedException ex){

            }
            finally{
                lock.unlock();
            }            
        }


        public void deposit(int amount){
            lock.lock();
            
            try{
                int newBalance = balance + amount;
                
                balance = newBalance;

                System.out.println("Deposit " + amount + "\t\t\t\t" + getBalance());


                newDeposit.signalAll();
            } 
            finally{
                lock.unlock();
            }
        }
    }
}