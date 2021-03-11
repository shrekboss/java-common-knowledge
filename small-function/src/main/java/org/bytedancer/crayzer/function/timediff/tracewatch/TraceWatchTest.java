package org.bytedancer.crayzer.function.timediff.tracewatch;

import cn.hutool.json.JSONUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yizhe.chen
 */
public class TraceWatchTest {
    public static void main(String[] args) throws InterruptedException {
        TraceWatch traceWatch = new TraceWatch();

        traceWatch.start("function1");
        TimeUnit.SECONDS.sleep(1);
        traceWatch.stop();

        traceWatch.start("function2");
        TimeUnit.SECONDS.sleep(1);
        traceWatch.stop();

        traceWatch.record("function1", 1);

        System.out.println(JSONUtil.parse(traceWatch.getTaskMap()));
    }
}
