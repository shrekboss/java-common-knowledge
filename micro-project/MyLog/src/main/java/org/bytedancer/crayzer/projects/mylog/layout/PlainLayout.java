package org.bytedancer.crayzer.projects.mylog.layout;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

/**
 * 纯文本布局，直接调用
 */
public class PlainLayout implements Layout {

    @Override
    public String doLayout(LoggingEvent e) {
        return e.toString();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
