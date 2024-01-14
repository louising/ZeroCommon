package com.zero.demo.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 bubble < select < insert < merge 
 */
public class Sorter {    
    public static void main(String[] args) throws Exception {
        //testSort();
        //prtVM();
        
        int[] arr = getNumbers(10, 100);
        prt(arr);
    }
    
    // 10 * 10000: Bubble 19s      Select 6.932s   Insert 3s       Merge 16ms Shell 15ms
    // 20 * 10000: Bubble 79.965s  Select 26.316s  Insert 13.159s  Merge 31ms Shell 31ms
    // 30 * 10000: Bubble 178.914s Select 57.362s  Insert 29.765s  Merge 63s  Shell 67ms 
    static void testSort() {
        /*
        for (int i = 0; i < 10; i++) {
            int count = 10_0000;
            //int[] arr = getNumbers(count, 1000_0000);
            int[] arr = new int[count];
            for (int k = count - 1; k >0; k--) {
                arr[k] = k;
            }
            
            int[] arr2 = new int[count];
            System.arraycopy(arr,  0,  arr2, 0,  count);
            int[] arr3 = new int[count];
            System.arraycopy(arr,  0,  arr3, 0,  count);

            long t = System.currentTimeMillis();
            String str = "";
            
            t = System.currentTimeMillis();
            shellSort(arr);
            str += " Shell: " + getCostTime(t);
            
            t = System.currentTimeMillis();
            mergeSort(arr2);
            str += " Merge: " + getCostTime(t);
            
            t = System.currentTimeMillis();
            quickSort(arr3);
            str += " Quick: " + getCostTime(t);
            
            System.out.println(str);
        }
        */
        
        int count = 30 * 10_000;
        int[] arr = getNumbers(count, 100 * 10000);
        int[] arr1 = new int[count], arr2 = new int[count], arr3 = new int[count], arr4 = new int[count], arr5 = new int[count], arr6 = new int[count];
        System.arraycopy(arr, 0, arr1, 0, count);
        System.arraycopy(arr, 0, arr2, 0, count);
        System.arraycopy(arr, 0, arr3, 0, count);
        System.arraycopy(arr, 0, arr4, 0, count);
        System.arraycopy(arr, 0, arr5, 0, count);
        System.arraycopy(arr, 0, arr6, 0, count);
        long t = System.currentTimeMillis();
        
        t = System.currentTimeMillis();
        bubbleSort(arr1);
        System.out.println("Bubble: " + getCostTime(t)); 

        t = System.currentTimeMillis();
        selectSort(arr2);
        System.out.println("Select: " + getCostTime(t));
        
        t = System.currentTimeMillis();
        insertSort(arr3);
        System.out.println("Insert: " + getCostTime(t));   

        t = System.currentTimeMillis();
        mergeSort(arr4);
        System.out.println("Merge: " + getCostTime(t));
        
        t = System.currentTimeMillis();
        shellSort(arr5);
        System.out.println("Shell: " + getCostTime(t));
    }

    
    //Find num in array, return index (-1 not found)
    public int binaryFindFromArray(int[] arr, int num) {
        int low = 0, high = arr.length - 1;
        while (true) {
            if (low > high) {
                return -1;
            }

            int mid = (low + high) / 2;
            if (arr[mid] == num)
                return mid;
            else if (arr[mid] < num) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
    }

    /** 
    bubbleSort(O(n^2): Move the larger to right, until the last position.
    Compare: O(n^2) Swap: O(n^2)
    
    Sort 10 * 10000 numbers: Bubble: 19s Select: 6s Insert:3s Merge: 16ms  
    */
    public static void bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
    selectSort: O(n^2) 
    Compare: O(N^2)  Swap: O(N)
    Find the index of the minimun element, for (i = 0; i < n-1; i++); 
     */
    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int currIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[currIndex] > arr[j]) {
                    currIndex = j;
                }
            }
            if (currIndex != i) {
                swap(arr, currIndex, i);
            }
        }
    }

    /** insertSort: O(n^2) */
    public static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i - 1;
            for (; j > -1 && arr[j] > tmp; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = tmp;
        }
    }
    
    //MergeSort 归并排序, 分治算法(divide-and-conquer algorithms), 
    //Drawback: Requires twice as much space as original array
    public static void mergeSort(int[] arr) {
        int[] arr2 = new int[arr.length];
        mergeRecur(arr, 0, arr.length - 1, arr2);
    }
    private static void mergeRecur(int[] arr, int start, int end, int[] arrNew) {
        if (start == end) 
            return;
        else {
            int mid = (start + end) / 2;
            mergeRecur(arr, start, mid,  arrNew);
            mergeRecur(arr, mid + 1, end, arrNew);
            mergeArr(arr, start, mid, mid + 1, end, arrNew);
        }
    }
    //Merge 2 sorted arrays to a new sorted array: arr1: arr[start1,end1], arr2: arr[start2,end2], new sorted array: arrNew
    private static void mergeArr(int[] arr, int start1, int end1, int start2, int end2, int[] arrNew) {
        //1. Merge 2 sorted arrays
        int i = start1, j = start2, k = 0;
        while (i <= end1 && j <= end2) {
            if (arr[i] < arr[j])
                arrNew[k++] = arr[i++];
            else
                arrNew[k++] = arr[j++];
        }
        while (i <= end1) {
            arrNew[k++] = arr[i++];
        }
        while (j <= end2) {
            arrNew[k++] = arr[j++];
        }
        
        //2. Copy combined array back to original array
        System.arraycopy(arrNew, 0, arr, start1, (end2-start1 + 1));
    }
    
    public static void shellSort(int[] arr) {
        //1) Get interval array; h = 3*h + 1; => 1 4 13 40 121
        int count = arr.length, h = 1;
        while ( (3 * h + 1 ) < count) {
            h = 3 * h + 1;
        }
        
        //2)   
        while (h > 0) {
            for (int i = h; i < arr.length; i++) {
                int tmp = arr[i], j = i - h; //first a[j] second a[inter + j] third a[a + 2* inter + j]
                for (; j > -1 && arr[j] > tmp; j -= h) {
                    arr[j+h]= arr[j];
                }
                arr[j + h] = tmp;
            }

            h = (h - 1) / 3;
        }
    }
    
    public static void quickSort(int[] arr) {
        recQuickSort(arr, 0, arr.length - 1);
    }
    
    /*
    private static void recQuickSort(int[] arr, int left, int right, int depth) {
        if (right - left <= 0) {
            return;
        } else {
            int mid = partition(arr, left, right);
            recQuickSort(arr, left, mid - 1, depth + 1);
            recQuickSort(arr, mid + 1, right, depth + 1);
            System.out.println("Call Stack Depth: " + depth);
        }
    }
    */
    private static void recQuickSort(int[] arr, int left, int right) {        
        if (right - left <= 0) {
            return;
        }
        else {
            int mid = partition(arr, left, right);
            recQuickSort(arr, left, mid - 1);
            recQuickSort(arr, mid + 1, right);
        }
    }
    
    public int partitionIt(int[] arr, int left, int right, long pivot) {
        int leftPtr = left - 1;
        int rightPtr = right + 1;
        while (true) {
            while (leftPtr < right && arr[++leftPtr] < pivot);
            while (rightPtr > left && arr[--rightPtr] > pivot);
            if (leftPtr >= rightPtr) // ==: if the pivot is the largest or smallest
                break;
            else
                swap(arr, leftPtr, rightPtr);
        }
        return leftPtr;
    }
    
    /* Use the median iteam as the pivot, to avoid StackOverflowError for sorted or inversed array larger than 7k. */  
    private static int partition(int[] arr, int left, int right) {
        int mid = getMid(arr, left, (left + right) / 2, right);
        swap(arr, mid, right);
        
        int pivot = arr[right];
        int leftPtr = left - 1, rightPtr = right;
        while (true) {
            while (arr[++leftPtr] < pivot); //As the right is pivot, so no need check right boundary
            while (rightPtr > 0 && arr[--rightPtr] > pivot);  //Need check boundary if first one is larger than pivot
            if (leftPtr < rightPtr)
                swap(arr, leftPtr, rightPtr);
            else
                break;
        }
        swap(arr, leftPtr, right);
        return leftPtr;
    }

    ////////////////////////////// Common functions //////////////////////////////
    /** Get 20 integers that between [0-100) */
    static int[] getNumbers(int count, int max) {
        //int[] arr = { 87,58,66,5,80 };
        //int[] arr = {87,58,66,5,80,12,9,49,76,61}; 10
        //int[] arr = new int[] {87,58,66,5,80,12,9,49,76,61,50,3,73,80,47,73,80,73,48,32}; //20
        int[] arr = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = random.nextInt(max);
        }

        return arr;
    }

    static void prt(int[] arr) {
        int wrapNumbers = 30;

        System.out.print("A=");
        for (int j = 0; j < arr.length; j++) {
            System.out.printf("%2d, ",arr[j]);
            if (j > 0 && (j + 1) % wrapNumbers == 0)
                System.out.println();
        }
        
        if ( arr.length % wrapNumbers > 0) {
            System.out.println();
        }
    }

    static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    static void prtCostTime(long start) {
        System.out.println(getCostTime(start));
    }
    
    static String getCostTime(long start) {
        long time = System.currentTimeMillis() - start;
        return (time >= 1000) ? (time / 1000) + "." + time % 1000 + "s" : time + "ms";
    }
    
    static int getMid(int arr[], int idx1, int idx2, int idx3) {
        /*
        System.out.println(getMid(new int[] { 1, 2, 3}, 0, 1, 2));
        System.out.println(getMid(new int[] { 1, 3, 2}, 0, 1, 2));
        System.out.println(getMid(new int[] { 2, 1, 3}, 0, 1, 2));
        System.out.println(getMid(new int[] { 2, 3, 1}, 0, 1, 2));
        System.out.println(getMid(new int[] { 3, 1, 2}, 0, 1, 2));
        System.out.println(getMid(new int[] { 3, 2, 1}, 0, 1, 2));
        */
        
        /*
        int a, b, c;
        int min = a < b ? (a < c ? a: c) : (b < c ? b: c);
        int max = a > b ? (a > c ? a: c) : (b > c ? b: c);
        int mid = a == min ? (b == max ? c : b) : (a == max ? (b == min ? c : b) : a);
        */
        
        /*
        int mid;
        if ((a < b && a > c) || (a < c && a > b))
            mid = a;
        else if ((b < a && b > c) || (b < c && b > a))
            mid = b;
        else
            mid = c;
        */
        
        int min = arr[idx1] <= arr[idx2] ? (arr[idx1] <= arr[idx3] ? idx1: idx3) : (arr[idx2] <= arr[idx3] ? idx2: idx3);
        int max = arr[idx1] >= arr[idx2] ? (arr[idx1] >= arr[idx3] ? idx1: idx3) : (arr[idx2] >= arr[idx3] ? idx2: idx3);
        int mid = arr[idx1] == arr[min] ? (arr[idx2] == arr[max] ? idx3 : idx2) : (arr[idx1] == arr[max] ? (arr[idx2] == arr[min] ? idx3 : idx2) : idx1);
        return mid;
    }

    static void prtVM() {
        Map<String, String> map = new HashMap<String, String>();
        
        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedMem = maxMem - freeMem;
        map.put("Thread.ID:", Thread.currentThread().getId() + " - " + Thread.currentThread().getName());
        map.put("CPU", Runtime.getRuntime().availableProcessors() + "");
        map.put("MaxMemory", maxMem + "MB");
        map.put("UsedMemory", usedMem + "MB");
        map.put("FreeMemory", freeMem + "MB");
        
        System.out.println(map);
    }
}
