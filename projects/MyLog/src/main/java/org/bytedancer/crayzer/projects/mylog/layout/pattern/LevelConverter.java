package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public class LevelConverter implements Converter {

    @Override
    public String convert(LoggingEvent e) {
        return e.getLevel().toString();
    }

}