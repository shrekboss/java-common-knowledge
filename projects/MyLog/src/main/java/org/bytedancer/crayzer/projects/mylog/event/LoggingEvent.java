package org.bytedancer.crayzer.projects.mylog.event;

import lombok.Data;
import lombok.ToString;
import org.bytedancer.crayzer.projects.mylog.enums.Level;

/**
 * @author yizhe.chen
 */
@Data
@ToString
public class LoggingEvent {

    public long timestamp;
    private Level level;
    private Object message;
    private String threadName;
    private long threadId;
    private String loggerName;

    public LoggingEvent(Level level, String msg, String name) {

    }
}
