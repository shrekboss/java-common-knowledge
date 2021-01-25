package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public class LoggerConverter extends KeywordConverter {

    @Override
    public String convert(LoggingEvent e) {
        return e.getLoggerName();
    }
}
