package unsynch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bank with a number of bank accounts.
 * @version 1.30 2004-08-01
 * @author Cay Horstmann
 */
public class Bank {
    private final double[] accounts;
    private Lock lock = new ReentrantLock();
    private Condition sufficientFunds = lock.newCondition();

    /**
    * Constructs the bank.
    * @param n the number of accounts
    * @param initialBalance the initial balance for each account
    */
    public Bank(int n, double initialBalance) {
        accounts = new double[n];
        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
    }

    /**
    * Transfers money from one account to another.
    * @param from the account to transfer from
    * @param to the account to transfer to
    * @param amount the amount to transfer
     * @throws InterruptedException 
    */
    public void transfer(int from, int to, double amount) throws InterruptedException {
        lock.lock();
        
        try {
            while (accounts[from] < amount)
                sufficientFunds.await();
            
            System.out.print(" " + Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d(%10.2f) to %d\n", amount, from, accounts[from], to);
            accounts[to] += amount;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
            sufficientFunds.signalAll();
        } finally {
            lock.unlock();
        }
        if (accounts[from] < amount)
            return;
    }

    /**
    * Gets the sum of all account balances.
    * @return the total balance
    */
    public double getTotalBalance() {
        double sum = 0;

        for (double a : accounts)
            sum += a;

        return sum;
    }
    
    public double getBalance(int account) {
        return accounts[account];
    }

    /**
    * Gets the number of accounts in the bank.
    * @return the number of accounts
    */
    public int size() {
        return accounts.length;
    }
}
