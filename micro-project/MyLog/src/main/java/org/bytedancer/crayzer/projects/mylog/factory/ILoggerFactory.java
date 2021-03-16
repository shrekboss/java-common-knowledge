package org.bytedancer.crayzer.projects.mylog.factory;

import org.bytedancer.crayzer.projects.mylog.ILogger;

/**
 * @author yizhe.chen
 */
public interface ILoggerFactory {
    /**
     * 通过class获取/创建logger
     */
    ILogger getLogger(Class<?> clazz);

    /**
     * 通过name获取/创建logger
     */
    ILogger getLogger(String name);

    /**
     * 通过name创建logger
     */
    ILogger newLogger(String name);
}
