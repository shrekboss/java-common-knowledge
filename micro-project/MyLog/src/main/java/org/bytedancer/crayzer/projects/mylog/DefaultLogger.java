package org.bytedancer.crayzer.projects.mylog;

import org.bytedancer.crayzer.projects.mylog.appender.IAppender;
import org.bytedancer.crayzer.projects.mylog.appender.impl.AppenderAttachableImpl;
import org.bytedancer.crayzer.projects.mylog.context.LoggerContext;
import org.bytedancer.crayzer.projects.mylog.enums.Level;
import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

/**
 * @author yizhe.chen
 */

public class DefaultLogger implements ILogger, ILifeCycle {

    private String name;
    private IAppender appender;
    /**
     * 当前 Logger 的级别，默认最低
     */
    private Level level = Level.TRACE;
    /**
     * 冗余级别字段，方便使用
     */
    private int effectiveLevelInt;

    private DefaultLogger parent;
    private AppenderAttachableImpl aai;
    private LoggerContext loggerContext;

    @Override
    public void trace(String msg) {
        filterAndLog(Level.TRACE, msg);
    }

    @Override
    public void info(String msg) {
        filterAndLog(Level.INFO, msg);
    }

    @Override
    public void warn(String msg) {
        filterAndLog(Level.WARN, msg);
    }

    @Override
    public void error(String msg) {
        filterAndLog(Level.ERROR, msg);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * 过滤并输出，所有的输出方法都会调用此方法
     **/
    private void filterAndLog(Level level, String msg) {
        LoggingEvent e = new LoggingEvent(level, msg, getName());
        // 循环向上查找可用的logger进行输出
        for (DefaultLogger l = this; l != null; l = l.parent) {
            if (l.appender == null) {
                continue;
            }
            if (level.toInt() > effectiveLevelInt) {
                l.appender.append(e);
            }
            break;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(Level level) {
        this.effectiveLevelInt = level.toInt();
        this.level = level;
    }

    public void setEffectiveLevelInt(int effectiveLevelInt) {
        this.effectiveLevelInt = effectiveLevelInt;
    }

    public void setParent(ILogger parent) {
        this.parent = (DefaultLogger) parent;
    }

    public void setAai(AppenderAttachableImpl aai) {
        this.aai = aai;
    }

    public void setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
