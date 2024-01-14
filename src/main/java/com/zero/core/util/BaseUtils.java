package com.zero.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


/**
 * Generic utility methods for working with usual operations.
 * 
 * @author Louis
 * @date
 */
@SuppressWarnings("unchecked")
public class BaseUtils {
    private static Random random = new Random();
    private static Calendar c = Calendar.getInstance();

    private static String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lowerChars = "abcdefghijklmnopqrstuvwxyz";

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private static NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     * Convery a Object to a array of byte[], this Object.class must implements
     * Serializable
     * 
     * @param if
     *            obj == null, return [-84, -19, 0, 5, 112]
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        byte[] bytes = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.writeObject(obj);
            oos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(oos);
            close(baos);
        }

        return bytes;
    }

    public static void close(java.io.Closeable resource) {
        if (resource != null)
            try {
                resource.close();
            } catch (IOException e) {
                Log.error(e);
            }
    }

    /**
     * Convert a byte array to a Object
     * 
     * @param bytes
     * @return Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getObject(byte[] bytes) {
        if (bytes == null)
            return null;

        Object obj = null;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(bytes)));
            obj = input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public static String getString(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(c);
        return sb.toString();
    }

    // print char: 32-126
    public static String nextString(int length, int first, int last) {
        char[] buf = new char[length];

        int range = last - first + 1;
        int nextChar;
        for (int i = 0; i < length; i++) {
            nextChar = random.nextInt(range) + first; // [0..last-first]
            buf[i] = (char) nextChar;
        }
        return new String(buf);
    }

    /**
     * Return string in (char)32..(char)126 with length
     * 
     */
    public static String nextString2(int length) {
        return nextString(length, 32, 126);
    }

    /**
     * Return string in (0..9,a..z,A..Z) with length
     */
    public static String nextString(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        char[] buf = new char[length];

        int nextChar;
        for (int i = 0; i < length; i++) {
            nextChar = random.nextInt(chars.length()); // [0..last)
            buf[i] = chars.charAt(nextChar);
        }
        return new String(buf);
    }

    public static double nextDouble() {
        return random.nextDouble();
    }

    public static int nextInt(int max) {
        return random.nextInt(max + 1);
    }

    public static int[] randomIntArray(int count, int max) {
        int[] arr = new int[count];
        for (int i = 0; i < count; i++) {
            arr[i] = nextInt(max);
        }
        return arr;
    }

    public static String getDoubleFormat(int scale) {
        StringBuffer sb = new StringBuffer("#,##0.");
        for (int i = 0; i < scale; i++)
            sb.append('0');

        return sb.toString();
    }

    /**
     * invoke method by reflect
     * 
     * @param methodName
     *            doIt
     * @param argsTypes
     *            new Class[] { int.class, String.class }
     * @param args
     *            new Object[] { 23, "Perfect" }
     */
    public static Object invoke(String methodName, Object object, Class<?>[] argsTypes, Object[] args) {
        Object result = null;

        try {
            Method m = object.getClass().getDeclaredMethod(methodName, argsTypes);
            m.setAccessible(true);
            result = m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object invoke(Method m, Object object) {
        return invoke(m, object, new Class[] {}, new Object[] {});
    }

    public static Object invoke(Method m, Object object, Class<?>[] argsTypes, Object[] args) {
        Object result = null;

        try {
            m.setAccessible(true);
            result = m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * clazz maybe not object's parent class 
     */
    public static Object invoke(Class<?> clazz, String methodName, Object object, Class<?>[] argsTypes, Object[] args) {
        Object result = null;

        try {
            Method m = clazz.getDeclaredMethod(methodName, argsTypes);
            m.setAccessible(true);
            result = m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object getFieldValue(Class<?> clzz, String fieldName, Object object) {
        Object obj = null;
        try {
            Field f = clzz.getDeclaredField(fieldName);
            f.setAccessible(true);
            obj = f.get(object);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Get field's value by reflect
     * 
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getFieldValue(String fieldName, Object object) {
        Object obj = null;
        Class<?> cls = object.getClass();
        Field field = null;
        try {
            field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            obj = field.get(object);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object getFieldValue(Field field, Object object) {
        Object fieldValue = null;
        try {
            field.setAccessible(true);
            fieldValue = field.get(object);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static void setFieldValue(String fieldName, Object fieldValue, Object object) {
        try {
            Field f = object.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, fieldValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object setFieldValue(Field field, Object fieldValue, Object object) {
        Object obj = null;
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object newInstance(Class<?> cls) {
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            Log.error(e);
        } catch (IllegalAccessException e) {
            Log.error(e);
        }

        return obj;
    }

    public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        c.set(year, month, date, hourOfDay, minute, second);
        return c.getTime();
    }

    public static Date nextDate() {
        int i = nextInt(10);
        return getDate(2008, 1, i);
    }

    public static String nextDateStr() {
        long now = System.currentTimeMillis();
        int delta = nextInt(10);
        System.out.println("Delta: " + delta);
        now = now + delta * (1000 * 60 * 60 * 24);
        return format(now);
    }

    public static Date getDate(int year, int month, int date) {
        c.set(year, month, date);
        return c.getTime();
    }

    public static int nextInt() {
        return random.nextInt(10);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 == null && obj2 == null) || (obj1 != null && obj2 != null && obj1.equals(obj2));
    }

    //Compare 2 object and sort null to the last
    @SuppressWarnings("rawtypes")
    public static int compareTo(Comparable obj1, Comparable obj2) {
        if (obj1 == null) {
            if (obj2 == null)
                return 0;
            else {
                return 1;
            }
        } else {
            if (obj2 == null)
                return -1;
            else {
                if (obj1 instanceof String && obj2 instanceof String)
                    return Collator.getInstance().compare((String) obj1, (String) obj2);
                else
                    return obj1.compareTo(obj2);
            }
        }
    }

    public static String[] getStrs(int count) {
        final int digit = getDigit(count);

        String[] strs = new String[count];
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        format.setMinimumIntegerDigits(digit); //13 -> 0013

        for (int i = 0; i < count; i++)
            strs[i] = "Item" + format.format(i);
        return strs;
    }

    /**
     * formate Date
     * @param pattern ddMMM(01Jun), ddMMMyy(01Jun06), ddMMMyyyy(01Jun2006)
     */
    public static String formatDate(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
            return dateFormat.format(date);
        } else
            return "NULL";

    }

    public static String formatDate(Date date) {
        if (date != null)
            return dateFormat.format(date);
        else
            return "NULL";
    }

    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * parse Date
     * @param ddMMM(01Jun), ddMMMyy(01Jun06), ddMMMyyyy(01Jun2006)
     */
    public static Date parseDate(String sDate, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
        Date d = null;
        try {
            d = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * @param minIntergerDigits 3: 1.0 -> 001.0     
     * @param minFractionDigits 5: 1.234 -> 1.23400  
     * @param maxFractionDigits 2: 1.234 -> 1.23
     */
    public static String format(double value, int minIntergerDigits, int minFractionDigits, int maxFractionDigits) {
        numberFormat.setMinimumIntegerDigits(minIntergerDigits);
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(minFractionDigits);
        numberFormat.setMaximumFractionDigits(maxFractionDigits);

        return numberFormat.format(value);
    }

    /**
     * format value
     * such as: 3 -> 003
     */
    public static String format(long value, int minIntergerDigits) {
        numberFormat.setMinimumIntegerDigits(minIntergerDigits);
        numberFormat.setGroupingUsed(false);

        return numberFormat.format(value);
    }

    /**
     * format value
     * such as: 3 -> 003
     */
    public static String format(int value, int minIntergerDigits) {
        return format(value, minIntergerDigits);
    }

    /**
     * @return time in format: 2008-04-27 19:19:58.656
     */
    public static String format(long time) {
        return String.format("%1$tF %1$tT.%1$tL", time); //2008-04-27 19:19:58.656
    }

    /**
     * @return current time in format: 2008-04-27 19:19:58.656
     */
    public static String currentTime() {
        return format(System.currentTimeMillis());
    }

    /**
     * @return time in format: 2007-02-06 15:30:27
     */
    public static String format(java.sql.Timestamp time) {
        String str = "";
        if (time != null)
            str = String.format("%1$tF %1$tT", time); // 2007-02-06 15:30:27
        return str;
    }

    /**
     * @return time in format: 2008-04-27 19:43:16 
     */
    public static String format(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * @return time in format: 2007-02-06 15:30:27 PST -0800
     */
    public static String format(Date time, boolean isGMT) {
        if (time == null)
            return "";

        if (isGMT) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time.getTime());
            c.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
            return String.format("%1$tF %1$tT %1$tZ %1$tz", c);
            //2007-07-11 06:11:31 GMT +0000
        } else
            return String.format("%1$tF %1$tT %1$tZ %1$tz", time);
    }

    /**
     * get the digit of a integer. 
     * such as: 123 -> 3, 1234 -> 4, 1234567 -> 7
     */
    public static int getDigit(long value) {
        if (value == 0)
            return 1;

        int digit = 0;
        value = Math.abs(value);
        while (value > 0) {
            digit++;
            value = value / 10;
        }
        return digit;
    }

    public static void delContent(String content, String fileName) throws IOException {
        //Read
        File f = new File(fileName);

        BufferedInputStream bin = null;
        OutputStream fout = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(f));
            byte[] buff = new byte[((int) f.length())];
            bin.read(buff);
            bin.close();
            String str = new String(buff, "gb2312");
            //String[] all = str.split("\r\n");

            int index = str.indexOf(content);

            if (index > 0) {
                str = str.substring(0, index);
                fout = new FileOutputStream(f);
                fout.write(str.getBytes("gb2312"));
                fout.flush();
                fout.close();
            }
        } catch (IOException e) {
            Log.error(e);
        } finally {
            if (bin != null)
                bin.close();
            if (fout != null)
                fout.close();
        }
    }

    /**
     * Del file(.extName) content in the directory
     */
    public static void delContent(String content, String dir, String... extNames) {
        List<String> fileNames = listFileNames(dir, extNames);

        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            try {
                delContent(content, fileName);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    /**
     * List all the file name in the path
     */
    public static List<String> listFileNames(final String path, final String... extNames) {
        List<String> fileFullNames = new ArrayList<String>();

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String fileName) {
                return isTheKindFile(fileName) || (new File(dir, fileName)).isDirectory();
            }

            boolean isTheKindFile(String name) {
                for (String s : extNames) {
                    if (name.endsWith(s) || name.toUpperCase().endsWith(s.toUpperCase()))
                        return true;
                }
                return false;
            }
        };

        File dir = new File(path);
        listFileNames(dir, filter, fileFullNames);

        return fileFullNames;
    }

    private static void listFileNames(File dir, FilenameFilter filter, List<String> fileFullNames) {
        String[] names = dir.list(filter);
        for (String s : names) {
            String fileName = dir.getPath() + "\\" + s;
            File file = new File(fileName);

            if (file.isDirectory())
                listFileNames(file, filter, fileFullNames);
            else
                fileFullNames.add(fileName);
        }
    }

    /**
     * Simple encoder, any char in source is standard ASCII char in [32, 126] 
     */
    public static String encode(String source) {
        //The range[row, high] must contains all char in the source
        //int low = 0, high = 65535, mid = (low + high) / 2;
        int low = 32, high = 126, mid = (low + high) / 2;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            int c = source.charAt(i);
            int x = c + mid;
            if (x > high)
                x = x % high + (low - 1);
            sb.append((char) x);
        }
        return sb.toString();
    }

    /**
     * Decoder for simple encoder
     */
    public static String decode(String str) {
        //The range[row, high] must contains all char in the source
        //int low = 0, high = 65535, mid = (low + high) / 2;
        int low = 32, high = 126, mid = (low + high) / 2;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            int x = c - mid;
            if (x < low)
                x = c - (low - 1) + high - mid;
            else
                x = c - mid;
            sb.append((char) x);
        }
        return sb.toString();
    }

    public static int hashCode(Object... objs) {
        int hashCode = 0;
        for (Object obj : objs) {
            hashCode ^= hashCode(obj);
        }
        return hashCode;
    }

    public static int hashCode(Object obj) {
        return (obj != null) ? obj.hashCode() : -1;
    }

    public static void prt(List<?> list) {
        System.out.println("Size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            System.out.printf("%3d: %s\n", i, obj);
        }
    }

    public static <T> void prtArray(T[] list) {
        System.out.println("Size: " + list.length);
        for (int i = 0; i < list.length; i++) {
            System.out.printf("%2d: %s\n", i, list[i]);
        }
    }

    public static void prtArray(int[] array) {
        prtArray(array, true);
    }
    
    public static void prtArray(int[] array, boolean isInOneLine) {
        if (isInOneLine) {
            System.out.printf("%s (%d) %n", Arrays.toString(array), array.length);
        } else {
            System.out.println("Size: " + array.length);
            for (int i = 0; i < array.length; i++) {
                System.out.printf("%2d: %s\n", i, array[i]);
            }
        }
    }

    public static void prt(Object... objs) {
        String str = "";
        for (Object obj : objs) {
            str += toString(obj) + " ";
        }
        System.out.println(str);
    }

    /** Deduce types: Thread */

    public static void prt(Object obj) {
        System.out.println(toString(obj));
    }

    public static void prt(int index, Object obj) {
        System.out.println(index + ": " + toString(obj));
    }

    public static String toString(Object obj) {
        String str = "";
        if (obj instanceof Thread) {
            Thread t = (Thread) obj;
            str = t.getId() + "," + t.getName() + "," + t.getPriority() + ": " + t.getState();
        } else if (obj instanceof int[]) {
            str = Arrays.toString((int[]) obj);
        } else if (obj instanceof Object[]) {
            str = Arrays.toString((Object[]) obj);
        } else
            str = obj + "";
        return str;
    }

    public static void prt(Collection<?> list) {
        System.out.println(list); //[1, 2, 3]
    }

    public static void prt(Map<?, ?> map) {
        System.out.println(map);
    }

    public static void prt(Iterator<?> iterator) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (iterator.hasNext())
            sb.append(iterator.next() + ", ");
        if (sb.length() > 1)
            sb.delete(sb.length() - 2, sb.length());
        sb.append(']');
        System.out.println(sb.toString());
    }

    /**
    @dirPath: c:/tmp
    Return <antcall target="_ZipProject"><param name="SourceDir" value="d:/tmp/1.txt"/></antcall>
    */
    public static void generateAntTaskOfZipProject(String dirPath) {
        File dir = new File(dirPath);
        String[] names = dir.list();

        String str = "";
        for (String name : names) {
            if (".metadata".equals(name))
                continue;
            String fileName = dirPath + "/" + name;
            str += "<antcall target=\"_ZipProject\"><param name=\"SourceDir\" value=\"" + fileName + "\"/></antcall>\n";
        }
        System.out.println(str);
    }

    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------    

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of whitespace.
     * <p><pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check whether the given String has actual text.
     * More specifically, returns <code>true</code> if the string not <code>null</code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p><pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its length is
     * greater than 0, and it does not contain whitespace only
     * @see java.lang.Character#isWhitespace
     */
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /** Return b9220e3a2dfe43cfb4b545789ec05d62 (32 bits) */
    public static String getId() {
        String token = UUID.randomUUID().toString(); //b9220e3a-2dfe-43cf-b4b5-45789ec05d62 (36 bits)
        System.out.println(token);                   //b9220e3a2dfe43cfb4b545789ec05d62 (32 bits)
        token = token.replace("-", "");
        System.out.println(token);
        return token;
    }

    /**
     * @return true if any one has no text
     */
    public static boolean hasBlankText(String... strs) {
        for (String str : strs) {
            if (!hasText(str))
                return true;
        }
        return false;
    }

    /**
     * Check whether the given String contains any whitespace characters.
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and
     * contains at least 1 whitespace character
     * @see java.lang.Character#isWhitespace
     */
    public static boolean containsWhitespace(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(String[] array, String s) {
        for (int i = 0; i < array.length; i++)
            if (array[i].equals(s))
                return i;
        return -1;
    }

    public static String format(MemoryUsage memoryUsage) {
        if (memoryUsage != null) {
            StringBuffer buf = new StringBuffer();
            long init = memoryUsage.getInit();
            long used = memoryUsage.getUsed();
            long committed = memoryUsage.getCommitted();
            long max = memoryUsage.getMax();

            buf.append(format("init", init));
            buf.append(format("used", used));
            buf.append(format("committed", committed));
            buf.append(format("max", max));
            return buf.toString();
        } else
            return "Null";
    }

    private static String format(String label, long value) {
        String s = String.format("%d(K), %d(M).", value >> 10, value >> 20);
        return String.format("%s=%-18s", label, s);
    }

    /**
     * Show byte to K/M
     * ...Method description goes here...
     * @param byes
     * @return
     */
    public static String formatByte(long bytes) {
        return String.format("%d/%d(K/M)", bytes >> 10, bytes >> 20);
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Log.error(e);
        }
    }

    public static boolean isStraightSequence(String source) {
        if (source != null && source.length() > 0) {
            int n = source.length();
            int prefix = Integer.valueOf(source.charAt(0) + "");
            for (int i = 1; i < n; i++) {
                int next = Integer.valueOf(source.charAt(i) + "");
                if (next == (prefix + 1)) {
                    prefix = next;
                    continue;
                } else
                    return false;
            }
        }
        return true;
    }

    public static boolean contain2Identical(String source) {
        if (source != null && source.length() > 2) {
            int n = source.length();
            int prefix = Integer.valueOf(source.charAt(0) + "");
            for (int i = 1; i < n; i++) {
                int next = Integer.valueOf(source.charAt(i) + "");
                if (next == prefix)
                    return true;
                else {
                    prefix = next;
                    continue;
                }
            }
        }

        return false;
    }

    /** Format to: HH:mm:ss */
    public static String formatTime(Date date) {
        if (date == null)
            return "";
        return timeFormat.format(date);
    }

    public static void prtUsedTime(String text, long start) {
        String str = getElapsedTime(start);
        prt(text + " " + str);
    }

    public static void prtElapsedTime(long start) {
        String str = getElapsedTime(start);
        prt("It took time: " + str);
    }

    /** Return 00:00:29.123 */
    public static String getElapsedTime(long start) {
        long t = System.currentTimeMillis() - start;
        String str = "00:";
        if (t >= 1000 * 60) {
            str += t / (1000 * 60) + ":";
            t = t % (1000 * 60);
            if (t >= 1000)
                str += t / 1000 + "." + t % 1000;
            else
                str += "00." + t;
        } else if (t >= 1000)
            str += "00:" + t / 1000 + "." + t % 1000;
        else
            str += "00:00." + t;
        return str;
    }

    public static String getCostTime(long start) {
        long time = System.currentTimeMillis() - start;
        String str = "";
        if (time < 1000)
            str = time + " ms";
        else if (time < 1000 * 60) {
            long seconds = time / 1000;
            long milliseconds = time % 1000;
            String ms = milliseconds < 10 ? ("00" + milliseconds) : (milliseconds < 100 ? "0" + milliseconds : "" + milliseconds);
            str = seconds + "." + ms + " seconds";
        } else
            str = (time / (60 * 1000)) + " minutes";
        return str;
    }

    public static void prtTime(long start) {
        System.out.println(getCostTime(start));
    }

    /** @param count =10, time: 3.828, count=100, time: 2:10.297 */
    public static void doSth(int count) {
        List<String> set = new ArrayList<String>();
        for (int i = 0; i < 1000000; i++) {
            String str = new String("A");
            for (int j = 0; j < count; j++)
                //j=10, time: 00:00:3.828; j=100, time: 00:2:10:297
                str += new String("B");

            set.add(str);
        }
    }

    /**<pre>
     * getString(65, 90): ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * getString(97,122): abcdefghijklmnopqrstuvwxyz </pre>
     */
    public static String getString(int start, int end) {
        String s = ""; //ABCDEFGHIJKLMNOPQRSTUVWXYZ
        for (int i = start; i <= end; i++)
            s += (char) i;
        return s;
    }

    public static String getRandomUserName(int length) {
        if (length < 2)
            return "";

        int FirstCharIndex = random.nextInt(upperChars.length());
        char c = upperChars.charAt(FirstCharIndex);
        String str = "" + c;
        for (int i = 1; i < length; i++) {
            int charIndex = random.nextInt(lowerChars.length());
            str += lowerChars.charAt(charIndex);
        }
        return str;
    }

    public static Map<String, String> getArgumentsOfVM() {
        Map<String, String> map = new HashMap<String, String>();
        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedMem = maxMem - freeMem;
        map.put("MaxMemory", maxMem + "MB");
        map.put("UsedMemory", usedMem + "MB");
        map.put("FreeMemory", freeMem + "MB");
        return map;
    }

    public static void prtVM() {
        Map<String, String> map = new HashMap<String, String>();

        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedMem = maxMem - freeMem;
        map.put("CPU", Runtime.getRuntime().availableProcessors() + "");
        map.put("MaxMemory", maxMem + "MB");
        map.put("UsedMemory", usedMem + "MB");
        map.put("FreeMemory", freeMem + "MB");

        System.out.println(map);
    }

    public static String getStr(Collection<?> c) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : c) {
            sb.append(obj + " ");
        }
        return sb.toString();
    }

    public static String sendGet(String urlStr, Map<String, String> headers) throws Exception {
        //System.out.println("GET " + urlStr + " Header: " + headers);

        StringBuffer sb = new StringBuffer();
        HttpURLConnection connection = (HttpURLConnection) (new URL(urlStr)).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true); //default: true(for input)
        connection.setConnectTimeout(30_000);
        connection.setReadTimeout(25_000);
        //connection.setUseCaches(false);
        //Set headers
        //connection.setRequestProperty("Content-Type", "application/json");
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }

        connection.connect(); //Open connect

        try (InputStream in = connection.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            //checkFileSize(in.available());
            readInputStreamToString(reader, sb);
        }

        connection.disconnect();

        return sb.toString();
    }

    /**<pre>
    * Read InputStream to String
    * This method is used to replace the following code:
    * 
    * String readLine;
    * while ((readLine = responseReader.readLine()) != null) {
    * sb.append(readLine).append("\n");
    * }
    * </pre>
    */
    private static void readInputStreamToString(BufferedReader br, StringBuffer sb) throws IOException {
        char[] c = new char[1024];
        int read = 0;
        while (read != -1) {
            read = br.read(c);
            if (read < 0) {
                break;
            }

            if (read < 1024) {
                char[] tmp = new char[read];
                tmp = Arrays.copyOf(c, read);
                sb.append(tmp);
            } else {
                sb.append(c);
            }
        }
    }
}
