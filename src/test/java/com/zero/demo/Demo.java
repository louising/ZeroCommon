package com.zero.demo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Demo {
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

    }

    static void prt(int[] arr) {
        for (int j = 0; j < arr.length; j++)
            System.out.print(arr[j] + " ");
        System.out.println("");
    }

    //@dirPath: G:\\workspace_3.5_rcp
    static void generateAntTaskOfZipProject(String dirPath) {
        File dir = new File(dirPath);
        String[] names = dir.list();

        String str = "";
        for (String name : names) {
            if (".metadata".equals(name))
                continue;
            String fileName = dirPath + "\\" + name;
            str += "<antcall target=\"_ZipProject\"><param name=\"SourceDir\" value=\"" + fileName + "\"/></antcall>\n";
        }
        System.out.println(str);
    }
}
