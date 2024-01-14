package com.zero.core.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//pom.xml: commons-codec/commons-codec:1.6
public class SecurityUtil {
    static String key = "Hint!sync#2020$1";  //16 bytes long
    
    public static String decrypt(String strIn, String strKey) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(strKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] bdata = strIn.getBytes();
        //byte[] encrypted1 = new Base64().decode(bdata);
        byte[] encrypted1 = Base64.getDecoder().decode(bdata);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "UTF-8");
        return originalString;
    }
    
    public static String encrypt(String strIn, String strKey) throws Exception {
        SecretKeySpec sKeySpec = new SecretKeySpec(strKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(strKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
        byte[] encrypted = cipher.doFinal(strIn.getBytes("UTF-8"));
        //return new String(new Base64().encode(encrypted));
        return new String(Base64.getEncoder().encode(encrypted));
    }
    
    public static void main(String[] args) throws Exception {
        String s1 = "Hello中国999!"; //"ABCD0123abcd中国北京!@#$%^&*()_+~{}|[]\\<>?,./`";
        String s2 = encrypt(s1, key); //3TIWXVJ47mNRD8d9kvO2bpQoqFSBsyEIfimIqwt9bJe1FkJpDGOrIRwXT/gsnTfXhE0=
        String s3 = decrypt(s2, key);
        System.out.println(s2);
        System.out.println(s3);
    }    
}
