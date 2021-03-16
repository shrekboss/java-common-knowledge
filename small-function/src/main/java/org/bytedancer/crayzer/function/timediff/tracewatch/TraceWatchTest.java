package org.bytedancer.crayzer.function.timediff.tracewatch;

import cn.hutool.json.JSONUtil;
import lombok.extern.java.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author yizhe.chen
 */
@Log
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

        log.info(JSONUtil.parse(traceWatch.getTaskMap()).toStringPretty());
    }
}
