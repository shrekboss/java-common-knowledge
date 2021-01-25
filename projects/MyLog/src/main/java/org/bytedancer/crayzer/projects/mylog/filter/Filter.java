package org.bytedancer.crayzer.projects.mylog.filter;

import org.bytedancer.crayzer.projects.mylog.ILifeCycle;
import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public interface Filter extends ILifeCycle {
    boolean doFilter(LoggingEvent event);

}