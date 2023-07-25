package com.example.demo.ConcurrentVsSynchronizedHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConcurrentHashMap {

    public static void main(String[] args) {
        Map<Integer, String> map = new java.util.concurrent.ConcurrentHashMap<>();
        map.put(1, "baeldung");
        map.put(2, "HashMap");
        for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
            map.put(3, "Modification");
            System.out.println(map);
        }
    }
}
