package org.bytedancer.crayzer.devmisuse.coding.concurrentTools.ciavspia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommonMistakesApplication {

    public static void main(String[] args) {
        // test(new HashMap<>());
        test(new ConcurrentHashMap<>());
    }

    private static void test(Map<String, String> map) {
        log.info("class : {}", map.getClass().getName());
        try {
            log.info("putIfAbsent null value : {}", map.putIfAbsent("test1", null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 当Key不存在的时候，putIfAbsent允许put null进去，而computeIfAbsent不能，之
        // 后进行containsKey查询是有区别的（当然了，此条针对HashMap，ConcurrentHashMap
        // 不允许put null value进去）
        log.info("test containsKey after putIfAbsent : {}", map.containsKey("test1"));
        log.info("computeIfAbsent null value : {}", map.computeIfAbsent("test2", k -> null));
        log.info("test containsKey after computeIfAbsent : {}", map.containsKey("test2"));
        // Key不存在的时候，putIfAbsent返回null，小心空指针，而computeIfAbsent返回计
        // 算后的值
        log.info("putIfAbsent non-null value : {}", map.putIfAbsent("test3", "test3"));
        log.info("computeIfAbsent non-null value : {}", map.computeIfAbsent("test4", k -> "test4"));
        // 当Key存在的时候，如果Value获取比较昂贵的话，putIfAbsent就白白浪费时间在获
        // 取这个昂贵的Value上（这个点特别注意）
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("putIfAbsent expensive value");
        log.info("putIfAbsent expensive value : {}", map.putIfAbsent("test4", getValue()));
        stopWatch.stop();
        stopWatch.start("computeIfAbsent expensive value");
        log.info("computeIfAbsent expensive value : {}", map.computeIfAbsent("test4", k -> getValue()));
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    private static String getValue() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }
}

