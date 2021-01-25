package org.bytedancer.crayzer.projects.mylog.layout;

import org.bytedancer.crayzer.projects.mylog.ILifeCycle;
import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public interface Layout extends ILifeCycle {

    String doLayout(LoggingEvent e);
}