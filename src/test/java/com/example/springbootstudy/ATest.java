package com.example.springbootstudy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author MCW 2023/11/1
 */
public class ATest {

    @Test
    public void test() {
        var h = new HashMap<String, String>();
        System.out.println(h.get("1"));
    }
}
