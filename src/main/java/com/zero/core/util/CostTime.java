package com.zero.core.util;

import static com.zero.core.util.BaseUtils.getCostTime;

public class CostTime {
    String time;
    int compareTimes;
    int swapTimes;

    public CostTime(long start) {
        super();
        this.time = getCostTime(start);
    }
    
    public CostTime(String time, int compareTimes, int swapTime) {
        super();
        this.time = time;
        this.compareTimes = compareTimes;
        this.swapTimes = swapTime;
    }

    public String toString() {
        return time; //"Cost: " + time + " Compare: " + compareTimes + " Swap: " + swapTimes;
    }
}
