package threaddemo;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadDemo {
    public static void main(String[] args) throws Exception {

        //prtTotalTimeInSingleThread(100, () -> { return slowMethodCostTime(); }); //145ms, 148ms 150ms
        //System.out.printf("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL T2 \n", System.currentTimeMillis());
        //prtTotalTimeMultiThread(1000, 100);

        prtCostTime(fact(2000));
        prtCostTime(fact2(2000));
    }

    static long fact(int n) {
        long t = System.currentTimeMillis();
        BigDecimal v = new BigDecimal(1);
        for (int i = 1; i <= n; i++) {
            v = v.multiply(new BigDecimal(i));
        }
        System.out.println("Fact " + n + ": " + v);
        return t;
    }

    static long fact2(int n) {
        long t = System.currentTimeMillis();
        BigDecimal v0 = new BigDecimal(1), v1 = new BigDecimal(1), v2 = new BigDecimal(1), v3 = new BigDecimal(1);
        for (int i = 1; i <= n; i += 4) {
            v0 = v0.multiply(new BigDecimal(i));
            v1 = v1.multiply(new BigDecimal(i + 1));
            v2 = v2.multiply(new BigDecimal(i + 2));
            v3 = v3.multiply(new BigDecimal(i + 3));
        }
        System.out.println("Fact " + n + ": " + v0.multiply(v1).multiply(v2).multiply(v3));
        return t;
    }

    static void prt(Object obj) {
        System.out.printf("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %2$s\n", System.currentTimeMillis(), obj);
    }

    static void prtTotalTimeInSingleThread(int times, Callable<Long> task) throws Exception {
        long t = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            task.call();
            //System.out.print("S(" + (System.currentTimeMillis() - t) + ")");            
        }
        //long avg = total / times;

        prtCostTime(System.currentTimeMillis() - t);
    }

    static void prtTotalTimeMultiThread(int taskCount, int threadCount) throws Exception {
        long t = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < taskCount; i++) {
            service.submit(() -> {
                slowMethodCostTime();
            });
        }
        service.shutdown();
        service.awaitTermination(100, TimeUnit.SECONDS);
        prtCostTime(System.currentTimeMillis() - t);
    }

    static void prtTotalTimeMultiThread2(int times, Callable<Long> task) throws Exception {
        long t = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < times; i++) {
            service.submit(task);
        }
        service.shutdown();
        service.awaitTermination(20, TimeUnit.SECONDS);
        prtCostTime(System.currentTimeMillis() - t);
    }

    //count 2000:3-8ms  5000:25-50ms  10000:130-200ms  20000:600-770 
    static long slowMethodCostTime() {
        long t = System.currentTimeMillis();
        int count = 10000, max = 100 * count;
        int[] arr = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = random.nextInt(max);
        }

        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
        return System.currentTimeMillis() - t;
    }

    static void prtCostTime(long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        String now = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL ", System.currentTimeMillis());
        String costTimeStr = costTime >= 1000 ? ((float) costTime / 1000 + "s") : (costTime + "ms");
        System.out.println(now + costTimeStr);
    }
}
