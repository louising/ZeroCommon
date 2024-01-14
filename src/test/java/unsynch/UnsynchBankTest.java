package unsynch;

import java.util.Arrays;
import java.util.Random;

/**
 * This program shows data corruption when multiple threads access a data structure.
 * @version 1.30 2004-08-01
 * @author Cay Horstmann
 */
public class UnsynchBankTest {
    public static final int NACCOUNTS = 100;
    public static final double INITIAL_BALANCE = 1000;

    public static void main(String[] args) {
        //sort1Bubble();
        
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++) {
            new Thread(new TransferRunnable(b, i, INITIAL_BALANCE)).start();
        }
    }
    
    
    public static void sort1Bubble() {
        long t = System.currentTimeMillis();
        int count = 10000;
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
