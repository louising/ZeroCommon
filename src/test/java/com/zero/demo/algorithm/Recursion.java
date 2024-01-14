package com.zero.demo.algorithm;

import java.util.Random;

public class Recursion {
    static Random random = new Random();
    static int count = 10;
    static int[] dataArr = new int[count];
    static {
        for (int i = 0; i < count; i++) {
            dataArr[i] = random.nextInt(100);
        }
    }

    public static void main(String[] args) throws Exception {
        String name = "cat";

        int length = name.length();
        int i = length;
        while (i > -1) {

        }
    }

    static void prtFactorial() {
        for (int i = 1; i < 10; i++) {
            System.out.print(factorial2(i) + " ");
        }
    }

    public static int factorial(int n) {
        int total = 1;

        while (n > 0) {
            total *= n--;
        }
        return total;
    }

    public static int factorial2(int n) {
        if (n == 1)
            return 1;

        return n * factorial2(n - 1);
    }

    public static int trigle(int n) {
        int total = 0;

        while (n > 0) {
            total += n--;
        }
        return total;
    }

    public static int trigle2(int n) {
        if (n == 1)
            return 1;

        return n + trigle2(n - 1);
    }

    static void prt(int[] arr) {
        for (int j = 0; j < arr.length; j++)
            System.out.print(arr[j] + " ");
        System.out.println("");
    }

}
