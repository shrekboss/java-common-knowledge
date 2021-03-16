package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public class LiteralConverter implements Converter {

    private String literal;

    @Override
    public String convert(LoggingEvent e) {
        return literal;
    }

    public LiteralConverter(String literal) {
        this.literal = literal;
    }
}
