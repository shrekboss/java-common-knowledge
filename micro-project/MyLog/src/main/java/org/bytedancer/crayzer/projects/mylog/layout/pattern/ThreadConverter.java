package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

/**
 * @author yizhe.chen
 */
public class ThreadConverter extends KeywordConverter {
    @Override
    public String convert(LoggingEvent e) {
        return e.getThreadName();
    }
}
