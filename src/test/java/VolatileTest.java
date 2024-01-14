import java.util.Arrays;
import java.util.Random;

public class VolatileTest  {
    private boolean initFlag = false;
    
    public void test() throws InterruptedException{
        Thread threadA = new Thread(() -> {
            while (!initFlag) {
     
            }
            String threadName = Thread.currentThread().getName();
            System.out.println("线程" + threadName+"获取到了initFlag改变后的值 " + initFlag);
        }, "threadA");
     
        //线程B更新全局变量initFlag的值
        Thread threadB = new Thread(() -> {
            initFlag = true;
        }, "threadB");
        
        //确保线程A先执行
        threadA.start();
        sort1Bubble();
        threadB.start();
    }
    
    public static void main(String[] args) throws InterruptedException {    
        new VolatileTest().test();
    }
    
    
    void sort1Bubble() {
        long t = System.currentTimeMillis();
        int count = 10_000; //10k 328ms
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
        System.out.println("Time(ms): " + milliSeconds); // + " Target: " + Arrays.toString(arr)
    }
}