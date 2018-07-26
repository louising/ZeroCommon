package com.zero.demo;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Algorithm_Sort {
    static Random random = new Random();

    static int count = 10;
    static int[] dataArr = new int[count];
    static {
        for (int i = 0; i < count; i++) {
            dataArr[i] = random.nextInt(100);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //testEncode();
        //generateAntTaskOfZipProject("F:\\_photos_20150313");

        prt(dataArr);
        insertSort(dataArr); //selectSort(dataArr);
        prt(dataArr);
    }    

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void prt(int[] arr) {
        for (int j = 0; j < arr.length; j++)
            System.out.print(arr[j] + " ");
        System.out.println("");
    }

    //Compare: O(N^2)  Swap: O(N)
    //Find the index of the minimun element, for (i = 0; i < n-1; i++); 
    public static void selectSort(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < length - 1; i++) {
            int index = i;
            for (int j = (i + 1); j < length; j++) {
                if (arr[j] < arr[index])
                    index = j;
            }
            if (index != i) {
                swap(arr, i, index);
            }
        }
    }

    //Suppose the left is order array, first has 1 element;
    public static void insertSort(int[] arr) {
        int length = arr.length;
        int orderArrLength = 1;

        for (int i = orderArrLength; i < length; i++) {
            int tmp = arr[i];
            int j = i - 1;
            for (; j > -1 && tmp < arr[j]; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = tmp;
            orderArrLength++;
        }
    }

    //Move the larger to right
    //Compare: O(n^2) Swap: O(n^2)
    public static void bubbleSort(int[] arr) {
        int length = arr.length;

        for (int out = length - 1; out > 1; out--)
            for (int in = 0; in < out; in++)
                if (arr[in] > arr[in + 1])
                    swap(arr, in, in + 1);
    }
}
