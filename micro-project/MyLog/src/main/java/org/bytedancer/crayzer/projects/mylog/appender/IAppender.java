package org.bytedancer.crayzer.projects.mylog.appender;

import org.bytedancer.crayzer.projects.mylog.ILifeCycle;
import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

/**
 * @author yizhe.chen
 */
public interface IAppender extends ILifeCycle {

    String getName();

    void setName(String name);

    void append(LoggingEvent e);
}
