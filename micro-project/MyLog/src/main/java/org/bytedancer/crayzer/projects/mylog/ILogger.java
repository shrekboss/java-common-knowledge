package org.bytedancer.crayzer.projects.mylog;

/**
 * @author yizhe.chen
 */
public interface ILogger {

    void trace(String msg);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

    String getName();
}
