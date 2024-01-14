package unsynch;

import java.util.Random;

/**
 * A runnable that transfers money from an account to other accounts in a bank.
 * @version 1.30 2004-08-01
 * @author Cay Horstmann
 */
public class TransferRunnable implements Runnable {
    private Bank bank;
    private int fromAccount;
    private double maxAmount;
    private int DELAY = 10;

    /**
    * Constructs a transfer runnable.
    * @param b the bank between whose account money is transferred
    * @param from the account to transfer money from
    * @param max the maximum amount of money in each transfer
    */
    public TransferRunnable(Bank b, int from, double max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    public void run() {
        try {
            while (true) {
                int toAccount = (int) (bank.size() * Math.random());
                double amount = maxAmount * Math.random();
                
                if (bank.getBalance(fromAccount) >= amount) {
                    sort1Bubble();
                    double balance = bank.getBalance(fromAccount);
                    if (balance < amount) {
                        System.err.println("From " + fromAccount  + " " + balance + " amount: " + amount);
                        System.exit(0);
                    }
                    bank.transfer(fromAccount, toAccount, amount);
                }
                Thread.sleep((int) (DELAY * Math.random()));
            }
        } catch (InterruptedException e) {
        }
    }
    
    void sort1Bubble() {
        long t = System.currentTimeMillis();
        int count = 5000;
        int[] arr = new int[count];
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = r.nextInt(count * 10);
        }
        //System.out.println("Source: " + Arrays.toString(arr));
        
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
        
        long milliSeconds = System.currentTimeMillis() - t;
        t = milliSeconds + 1; //cyclic reference
        //System.out.println(milliSeconds + " Target: " + Arrays.toString(arr));
    }
}
