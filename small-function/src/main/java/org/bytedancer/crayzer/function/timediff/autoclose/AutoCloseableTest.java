package org.bytedancer.crayzer.function.timediff.autoclose;

import cn.hutool.json.JSONUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author yizhe.chen
 */
public class AutoCloseableTest {
    public static void main(String[] args) throws Exception {
        TraceWatch traceWatch = new TraceWatch();

        try (TraceWatch ignored = traceWatch.start("function1")) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        try (TraceWatch ignored = traceWatch.start("function2")) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        try (TraceWatch ignored = traceWatch.start("function1")) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(JSONUtil.parse(traceWatch.getTaskMap()));
    }

}
