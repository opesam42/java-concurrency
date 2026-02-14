import java.util.concurrent.Semaphore;

public class SemaphoreAcc {
    
    public static void main(String[] args) {
        Account gbengaAccount = new Account();

        System.out.println("balance:" + gbengaAccount.getBalance());

        gbengaAccount.getSalary();       
        System.out.println("balance:" + gbengaAccount.getBalance());
    }

    private static class Account{
        private static Semaphore semaphore = new Semaphore(1);
        private int balance = 0;

        public int getBalance(){
            return balance;
        }

        public void deposit(int amount){
            try{
                semaphore.acquire();    // acquire lock
                int newBalance = balance + amount;

                Thread.sleep(5);

                balance = newBalance;
            } catch(InterruptedException e){

            }finally{
                semaphore.release();
            } 
        }

        public void getSalary(){
            deposit(55000);
        }
    }
}
