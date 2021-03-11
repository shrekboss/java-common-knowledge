package org.bytedancer.crayzer.function.timediff.function;

import org.bytedancer.crayzer.function.timediff.tracewatch.TraceWatch;

import java.util.function.IntConsumer;
import java.util.function.Supplier;

/**
 * @author yizhe.chen
 */
public class TraceHolder {

    private TraceHolder() {}

    /**
     * 有返回值调用
     */
    public static <T> T run(TraceWatch traceWatch, String taskName, Supplier<T> supplier) {
        try {
            traceWatch.start(taskName);
            return supplier.get();
        } finally {
            traceWatch.stop();
        }
    }

    /**
     * 无返回值调用
     */
    public static void run(TraceWatch traceWatch, String taskName, IntConsumer function) {
        try {
            traceWatch.start(taskName);
            function.accept(0);
        } finally {
            traceWatch.stop();
        }
    }
}
