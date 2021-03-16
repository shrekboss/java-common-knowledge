package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

/**
 * @author yizhe.chen
 */
public class LineSeparatorConverter extends KeywordConverter {
    @Override
    public String convert(LoggingEvent e) {
        return System.lineSeparator();
    }
}
