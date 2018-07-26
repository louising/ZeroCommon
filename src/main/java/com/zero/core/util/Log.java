package com.zero.core.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A simple Logger to print information to standard output stream.
 * Just for testing, set the level to Level.OFF to close this log.
 * 
 * @author Louis
 */
public class Log {
    private static final String prefix = "###";

    private static Level level = Level.DEBUG;
    private static boolean isWriteToFile = false; //write to c:/test.log
    private static PrintWriter pw;

    static {
        try {
            //append
            Writer writer = new OutputStreamWriter(new FileOutputStream("c:/test.log", true));
            //auto-flush
            pw = new PrintWriter(writer, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //### [DEBUG][2007-12-17 14:16:23.703][com.zero.demo.Test.main(19)]: Hello 
    private static void writeMsg(Level level, String msg) {
        if (level.compareTo(Level.OFF) == 0 || level.compareTo(Log.level) < 0)
            return;

        //getStackTrace()->writeMsg()->info()->main()
        StackTraceElement[] a = Thread.currentThread().getStackTrace();

        String message = "";
        if (a != null && a.length > 3) {
            StackTraceElement e = a[3];
            String clsName = e.getClassName();
            //int index = clsName.lastIndexOf(".");
            //clsName = clsName.substring(index + 1);
            String methodName = e.getMethodName();
            int lineNumber = e.getLineNumber();
            String time = BaseUtils.currentTime();
            //out.printf("%s [%s] %s (%s.%s:%d) - %s\n", prefix, level, time, clsName, methodName, lineNumber, msg);
            message = String.format("%s [%s] %s (%s.%s:%d) - %s", prefix, level, time, clsName, methodName, lineNumber, msg);
        } else {
            String clsName = "Unknown Source";
            String time = BaseUtils.currentTime();
            //out.printf("%s [%s][%s][%s]: %s \n", prefix, level, time, clsName, msg);
            message = String.format("%s [%s][%s][%s]: %s", level, time, clsName, msg);
        }
        if (isWriteToFile)
            pw.println(message);
        else
            System.out.println(message);
    }

    /**
     * Log debug message
     * 
     * @param msg "user %s is deleted %d"
     * @param args "user001", 123
     */
    public static void debug(String msg, Object... args) {
        if (args != null && args.length > 0)
            msg = String.format(msg, args);

        writeMsg(Level.DEBUG, msg);
    }

    public static void debug(Object message) {
        String msg = message == null ? "" : message.toString();
        writeMsg(Level.DEBUG, msg);
    }

    public static void info(Object message) {
        String msg = (message != null) ? message.toString() : "null";
        writeMsg(Level.INFO, msg);
    }

    public static void info() {
        prt();
    }

    public static void prt() {
        System.out.println();
    }

    public static void prt(Object message) {
        System.out.println(message);
    }

    public static void prt(String message, Object... args) {
        System.out.printf(message, args);
    }

    public static void prt(List<?> list) {
        if (list == null) {
            System.out.println("List is null");
            return;
        }

        System.out.println("List size: " + list.size());
        for (int i = 0; i < list.size(); i++)
            //System.out.println(list.get(i));
            System.out.printf("%3d) %s\n", i, list.get(i));
    }

    public static void prt(Collection<?> collection) {
        if (collection == null) {
            System.out.println("List is null");
            return;
        }

        System.out.println("Size: " + collection.size());
        Iterator<?> it = collection.iterator();
        for (int i = 0; it.hasNext(); i++)
            System.out.println(i + ": " + it.next());
    }

    public static <T> void prt(T[] array) {
        if (array == null) {
            System.out.println("Array is null");
            return;
        }

        System.out.println("Size: " + array.length);
        for (int i = 0; i < array.length; i++)
            System.out.println(array[i]);
    }

    public static void info(String message, Object... args) {
        String msg = message;
        if (args != null && args.length > 0)
            msg = String.format(message, args);

        writeMsg(Level.INFO, msg);
    }

    public static void warn(String message) {
        writeMsg(Level.WARN, message);
    }

    public static void error(String message, Object... args) {
        String msg = message;
        if (args != null && args.length > 0)
            msg = String.format(message, args);

        writeMsg(Level.ERROR, msg);
    }

    public static void error(Exception e) {
        writeMsg(Level.ERROR, e.getMessage());
    }

    public static void fatal(String message) {
        writeMsg(Level.FATAL, message);
    }
}

/**
 * Log level
 */
enum Level {
    OFF("OFF"), DEBUG("DEBUG"), INFO("INFO"), WARN("WARN"), ERROR("* ERROR *"), FATAL("**FATAL**");

    private String message;

    private Level(String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
