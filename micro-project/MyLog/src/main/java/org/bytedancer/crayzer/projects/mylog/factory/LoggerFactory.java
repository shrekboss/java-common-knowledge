package org.bytedancer.crayzer.projects.mylog.factory;

import org.bytedancer.crayzer.projects.mylog.ILogger;

/**
 * @author yizhe.chen
 */
public class LoggerFactory {

    private static ILoggerFactory loggerFactory = new StaticLoggerFactory();

    public static ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public static ILogger getLogger(Class<?> clazz) {
        return getLoggerFactory().getLogger(clazz);
    }

    public static ILogger getLogger(String name) {
        return getLoggerFactory().getLogger(name);
    }
}
