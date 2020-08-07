package org.bytedancer.crayzer.common_dev_error.troubleshootingtools.jdktool;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CommonMistakesApplication {

    public static void main(String[] args) throws InterruptedException {
        //wrong: java -jar common-mistakes-0.0.1-SNAPSHOT.jar -Xms1g -Xmx1g // 位置错误
        //right: java -Xms1g -Xmx1g -jar common-mistakes-0.0.1-SNAPSHOT.jar

        // ThreadStackSize 参数的单位是 KB
        //wrong: java -XX:ThreadStackSize=256k common-mistakes-0.0.1-SNAPSHOT.jar
        //right: java -XX:ThreadStackSize=256 common-mistakes-0.0.1-SNAPSHOT.jar

        // jstat -gcutil 23940 5000 100 // 每隔 5 秒输出一次，输出 100 次

        // jcmd 24781 help
        // GC.heap_info 命令可以打印 Java 堆的一些信息
        // jcmd 24404 VM.native_memory summary //
        // 通过 NMT，可以观察细粒度内存使用情况
        //-Xms1g -Xmx1g -XX:NativeMemoryTracking=summary | detail
        System.out.println("VM options");
        System.out.println(ManagementFactory.getRuntimeMXBean().getInputArguments().stream().collect(Collectors.joining(System.lineSeparator())));
        System.out.println("Program arguments");
        System.out.println(Arrays.stream(args).collect(Collectors.joining(System.lineSeparator())));

        IntStream.rangeClosed(1, 10).mapToObj(i -> new Thread(() -> {
            while (true) {
                String payload = IntStream.rangeClosed(1, 10_000_000) // 10M
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(payload.length());
            }
        })).forEach(Thread::start);

        TimeUnit.HOURS.sleep(1);
    }
}

