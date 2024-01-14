import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.zero.core.util.BaseUtils;

class FileInfo {
    String name;
    long size;
    int count = 1;

    public FileInfo(String name, long size) {
        super();
        this.name = name;
        this.size = size;
    }
}

public class FileInputStreamDemo {
    static int num = 0;
    static AtomicInteger rowNum = new AtomicInteger(0);
    static List<String> concurrList = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String[] args) throws Exception {
        //checkImageExt();        
        //testCurrency();
        //BigDecimal b = new BigDecimal("");
        //System.out.println(b);
        System.out.println(DummyX.calc(2, 3));
    }
    
    static void testCurrency() throws Exception {
        long t = System.currentTimeMillis();
        testRestfulServiceMultiConcurrent(1000, 20);

        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        prtResultByTime();
        long time = System.currentTimeMillis() - t;
        System.out.println("Real cost time: " + ((time >= 1000) ? (time / 1000) + "." + (time % 1000) + "s" : time + "ms") );
    }
    
    //count 2000:3-8ms  5000:25-50ms  10000:130-200ms  20000:600-770 
    public static long slowMethodCostTime() {
        long t = System.currentTimeMillis();
        int count = 10000, max = 1000_0000;
        int[] arr = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = random.nextInt(max);
        }
        
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
        return System.currentTimeMillis() - t;
    }
    
    static void testRestfulServiceMultiConcurrent(int requestCount, int threadCount) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < requestCount; i++) {
            service.submit(() -> {
                long startTime = System.currentTimeMillis();
                //System.out.printf("Send Req %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL \n", startTime);
                try {
                    String result = BaseUtils.sendGet("http://localhost:7901/user/sayHello", null);
                    //String result = BaseUtils.sendGet("http://localhost:8001/hello", null);
                    //String result = BaseUtils.sendGet("http://localhost:8003/hello", null);
                    prtResponse(startTime, result);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } );
        }
        service.shutdown();
        //service.awaitTermination(20, TimeUnit.SECONDS); //Task finished or timeout arrives tasks may be not finished 
        /*
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    String result = BaseUtils.sendGet("http://localhost:7901/user/sayHello", null);
                    //String result = BaseUtils.sendGet("http://localhost:8001/hello", null);
                    //String result = BaseUtils.sendGet("http://localhost:8003/hello", null);
                    prtResponse(startTime, result);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
            //Thread.sleep(100);
        }
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        long t = System.currentTimeMillis();
        */
    }

    //png: 89504e470d0a1a0a0000
    //jpg: ffd8ffe
    //gif: 474946383961
    static void checkImageExt() throws Exception {
        File dir = new File("c:/tmp");
        File[] files = dir.listFiles();
        for (File file : files) {
            //ImageInputStream in = ImageIO.createImageInputStream(file);
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[20];
            in.read(bytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {                
                int v = bytes[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    sb.append(0);
                }
                sb.append(hv);
            }
            System.out.printf("%s \t\t %s \n", file.getName(), sb);
            in.close();
        }
    }


    /** Return "" if error */
    static String getImageExtName(File file) throws Exception {
        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (iter.hasNext()) {
            return iter.next().getFormatName();
        }
        return "";
    }

    //path: d:/tmp  newName: daily
    static void renames(String path, String newName) throws Exception {
        File dir = new File(path);
        File[] files = dir.listFiles();
        int size = files.length;
        for (int i = 0; i < size; i++) {
            if (files[i].isFile()) {
                String fileName = files[i].getName();
                String extName = fileName.substring(fileName.lastIndexOf("."));
                files[i].renameTo(new File(files[i].getParent() + File.separator + newName + (i + 1) + extName));
            }
        }
    }

    static boolean contains(String source, String strList) {
        String[] strs = strList.split(" ");
        for (String s : strs) {
            if (source.contains(s))
                return true;
        }
        return false;
    }

    public static void listFiles(File directory, Map<String, Integer> map) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                listFiles(file, map);
            else {
                /*
                if (contains(file.getAbsolutePath(), "JnlpGenerator RcpShared.WebStart.ant FirstServlet nav_bar_img_horizontal nav_bar_img_vertical HomeDemo"))
                    continue;
                if ("web.txt index.jsp readme.txt build.bat deploy.bat clear.bat readme.txt .project .classpath index.txt".contains(file.getName())) {
                    continue;
                }
                */

                String key = file.getName() + " " + file.length();
                //String key = file.getName();
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                    //System.out.println("Delete " + file.getAbsolutePath() + " " + file.delete());
                    System.out.println(file.getName() + " " + (map.get(key) + 1));
                } else {
                    map.put(key, 1);
                }
            }
        }
    }

    public static void listFiles(File directory, BlockingQueue<File> queue) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                listFiles(file, queue);
            else
                queue.put(file);
        }
    }


    protected static void prtResultByTime() {
        Map<String, List<String>> map = new TreeMap<>();
        for (String str : concurrList) {
            int index = str.indexOf("Begin");
            String s = str.substring(index, index + 25);
            if (!map.containsKey(s)) {
                map.put(s, new ArrayList<String>());
            }
            map.get(s).add(str);
        }

        for (List<String> list : map.values()) {
            for (String s : list) {
                System.out.printf("%3d: %s \n", num++, s);
            }
            System.out.println();
        }
    }

    // Max 59 Min 6 Avg 15
    static void testMultiTimes() {
        long max = 0, min = 0, total = 0, avg = 0;
        String str = "";

        int count = 100;
        for (int i = 0; i < count; i++) {
            //long t = System.currentTimeMillis(); //prtStartTime();
            prtHeap();
            testRAM();
            prtHeap();

            long tmp = 0; //prtResponse(t);
            str += tmp + " ";
            if (max == 0)
                max = tmp;
            else {
                if (tmp > max)
                    max = tmp;
            }
            if (min == 0)
                min = tmp;
            else if (tmp < min)
                min = tmp;
            total += tmp;

            System.out.println();
        }
        avg = total / count;
        System.out.printf("### Max %d Min %d Avg %d %s \n", max, min, avg, str);
    }

    //Max 59 Min 6 Avg 15  Xmx 70M
    static void testRAM() {
        StringBuilder sb = new StringBuilder();
        String str = "Many nights we prayed. With no proof anyone could hear.And our hearts a hopeful song. We barely understood Now we are not afraid";
        for (int i = 0; i < 15 * 8192; i++) { //8K  15M=15 * 8192
            sb.append(str);
        }
    }

    static void testWriteStream() throws Exception {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("d:/tmp/1.txt"), "UTF-8"), false)) {
            for (int i = 0; i < 524288; i++) { //1M 1048576 500K 
                if (i % 10000 == 0) {
                    Thread.sleep(500);
                }
                out.println("The quick brown fox jumps over the lazy dog.");
            }
            out.flush(); //No reed to flush() as the constructor parameter auflush = true
        }
    }

    static void testReadStream() throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("d:/tmp/1.txt")))) {
            String str;
            while ((str = reader.readLine()) != null) {
                if (rowNum.intValue() % 2000 == 0) {
                    Thread.sleep(100);
                }
                System.out.println(rowNum.intValue() + ": " + str);
                sb.append(str);
            }
            System.out.println("Length(M): " + sb.length() / (1024 * 1024));
        }
    }

    public static void prtResponse(long startTime, String result) {
        long time = System.currentTimeMillis() - startTime;
        String costTimeStr = (time >= 1000) ? (time / 1000) + "." + (time % 1000) + "s" : time + "ms";

        String startTimeStr = String.format("Req: %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL ", startTime);
        String endTimeStr = String.format("Res: %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL ", System.currentTimeMillis());
        String str = String.format("Invoke %s %s CostTime %6s Result: %s", startTimeStr, endTimeStr, costTimeStr, result);

        concurrList.add(str);
        //System.out.println(str);
    }

    public static void prtEndTime(long startTime) {
        long time = System.currentTimeMillis() - startTime;
        String costTimeStr = (time >= 1000) ? (time / 1000) + "." + (time % 1000) + "s" : time + "ms";

        String startTimeStr = String.format("Req: %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL ", startTime);
        String endTimeStr = String.format("Res: %1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL ", System.currentTimeMillis());
        String str = String.format("%s %s CostTime %6s", startTimeStr, endTimeStr, costTimeStr);

        System.out.println(str);
    }

    static void prtHeap() {
        Map<String, String> map = new HashMap<String, String>();

        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);

        map.put("CPU", Runtime.getRuntime().availableProcessors() + "");
        map.put("MaxMemory", maxMem + "M");
        map.put("CommitedMemory", total + "M");
        map.put("FreeMemory", freeMem + "M");

        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        map.put("Used", mbean.getHeapMemoryUsage().getUsed() / (1024 * 1024) + "M");
        map.put("Commited", mbean.getHeapMemoryUsage().getCommitted() / (2024 * 1024) + "M");
        map.put("Max", mbean.getHeapMemoryUsage().getMax() / (1024 * 1024) + "M");

        System.out.println(map);
    }
}
