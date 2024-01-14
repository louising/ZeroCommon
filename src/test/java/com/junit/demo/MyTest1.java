package com.junit.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyTest1 {

    @Test
    void testMethod1() {
        int c = 1 + 2;
        Assertions.assertTrue(c == 3);
    }
}
