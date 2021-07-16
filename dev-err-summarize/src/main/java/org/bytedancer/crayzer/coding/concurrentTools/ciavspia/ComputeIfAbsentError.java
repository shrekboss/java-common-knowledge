package org.bytedancer.crayzer.coding.concurrentTools.ciavspia;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yizhe.chen
 */
public class ComputeIfAbsentError {

    /**
     * ConcurrentHashMap中computeIfAbsent死循环bug问题
     *
     * http://www.111com.net/jsp/189310.htm
     */
    public static void main(String[] args) {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("AaAa",key->map.computeIfAbsent("BBBB",key2->42));
        System.out.println(map);
    }
}
