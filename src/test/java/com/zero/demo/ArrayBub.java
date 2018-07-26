package com.zero.demo;

import java.util.Random;

class ArrayBub {
    private long[] a;
    private int nElems;

    public ArrayBub(int max) {
        a = new long[max];
        nElems = 0;
    }

    public void insert(long value) {
        a[nElems++] = value;
    }

    public void display() {
        for (int j = 0; j < nElems; j++)
            System.out.print(a[j] + " ");
        System.out.println("");
    }

    public void insertionSort() {
        int in, out;

        for (out = 1; out < nElems; out++) {
            long temp = a[out];
            in = out;
            while (in > 0 && a[in - 1] >= temp) {
                a[in] = a[in - 1];
                --in;
            }
            a[in] = temp;
        }
    }

    public void bubbleSort() {
        int out, in;

        for (out = nElems - 1; out > 1; out--)
            for (in = 0; in < out; in++)
                if (a[in] > a[in + 1])
                    swap(in, in + 1);
    }

    private void swap(int one, int two) {
        long temp = a[one];
        a[one] = a[two];
        a[two] = temp;
    }

    public static void main(String[] args) {
        Random r = new Random();
        int count = 10;
        ArrayBub mm = new ArrayBub(count);
        for (int i = 0; i < count; i++) {
            mm.insert(r.nextInt(100));
        }
        mm.display();
        mm.bubbleSort();
        mm.display();
    }
}