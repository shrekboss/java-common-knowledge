package org.bytedancer.crayzer.function.timediff.function;

import cn.hutool.json.JSONUtil;
import org.bytedancer.crayzer.function.timediff.tracewatch.TraceWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author yizhe.chen
 */
public class TraceHolderTest {

    public static void main(String[] args) {
        TraceWatch traceWatch = new TraceWatch();

        TraceHolder.run(traceWatch, "function1", i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        String result = TraceHolder.run(traceWatch, "function2", () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return "YES";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "NO";
            }
        });

        TraceHolder.run(traceWatch, "function1", i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(JSONUtil.parse(traceWatch.getTaskMap()));
    }
}
