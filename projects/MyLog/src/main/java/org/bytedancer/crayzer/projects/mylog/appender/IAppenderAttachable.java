package org.bytedancer.crayzer.projects.mylog.appender;

/**
 * @author yizhe.chen
 */
public interface IAppenderAttachable {

    void addAppender(IAppender newAppender);

    IAppender getAppender(String name);

    boolean isAttached(IAppender appender);

    void removeAppender(IAppender appender);

    void removeAppender(String name);
}
