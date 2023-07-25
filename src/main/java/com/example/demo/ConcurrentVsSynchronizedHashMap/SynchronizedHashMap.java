package com.example.demo.ConcurrentVsSynchronizedHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedHashMap {

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "baeldung");
        map.put(2, "HashMap");
        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(map);
        for (Map.Entry<Integer, String> integerStringEntry : synchronizedMap.entrySet()) {
            synchronizedMap.put(3, "Modification");
        }
    }

}
