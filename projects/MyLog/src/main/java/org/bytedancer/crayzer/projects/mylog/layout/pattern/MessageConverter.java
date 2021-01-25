package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public class MessageConverter extends KeywordConverter {
    @Override
    public String convert(LoggingEvent e) {
        return String.valueOf(e.getMessage());
    }
}
