public class SharedData {
    //private boolean isRunning = true;
    private volatile boolean isRunning = true;

    public void stop() {
        isRunning = false;
    }

    public void doWork() {
        long counter = 0;
        while (isRunning) {
            counter++;
            //System.out.println(Thread.currentThread().getName() + " Working...");
        }
        System.out.println(Thread.currentThread().getName() + " Work stopped. Counter: " + counter);
    }
    
    static void testVolatile() {
        System.out.println("Begin ");
        SharedData sharedData = new SharedData();

        // Create multiple threads
        Thread thread1 = new Thread(() -> sharedData.doWork());
        Thread thread2 = new Thread(() -> sharedData.doWork());

        // Start the threads
        thread1.start();
        thread2.start();

        try {
            // Sleep for 1 second
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Stop the threads
        sharedData.stop();

        try {
            // Wait for the threads to complete
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("End.");
    }

    public static void main(String[] args) {
/*        for (int i = 0; i < 10; i++) {
            testVolatile();
        }*/
    }
    
    static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }
}
