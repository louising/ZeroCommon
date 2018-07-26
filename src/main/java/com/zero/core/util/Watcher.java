package com.zero.core.util;

public class Watcher {
    static long lastTime = System.currentTimeMillis();
    
    public static void mark() {
        mark("Now");
    }
    
    public static void mark(String taskName) {
        long now = System.currentTimeMillis();
        String currTime = BaseUtils.format(lastTime);
        long interval = now - lastTime;
        Log.info("[%s]: %s (%d/%d)", taskName, currTime, interval, interval/1000);
        
        lastTime = now;        
    }
}
