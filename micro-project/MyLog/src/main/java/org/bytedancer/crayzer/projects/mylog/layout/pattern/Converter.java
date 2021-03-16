package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

public interface Converter {

    String convert(LoggingEvent e);

}
