package org.bytedancer.crayzer.projects.mylog.appender.impl;

import org.bytedancer.crayzer.projects.mylog.appender.IAppender;
import org.bytedancer.crayzer.projects.mylog.appender.IAppenderAttachable;
import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yizhe.chen
 */
public class AppenderAttachableImpl implements IAppenderAttachable {

    final private List<IAppender> appenderList = new CopyOnWriteArrayList<>();

    @Override
    public void addAppender(IAppender newAppender) {
        appenderList.add(newAppender);
    }

    public int appendLoopOnAppenders(LoggingEvent event) {
        int size = 0;
        IAppender appender;

        if (appenderList != null) {
            size = appenderList.size();
            for (int i = 0; i < size; i++) {
                appender = appenderList.get(i);
                appender.append(event);
            }
        }
        return size;
    }

    @Override
    public IAppender getAppender(String name) {
        if (name == null) {
            return null;
        }
        for (IAppender appender : appenderList) {
            if (name.equals(appender.getName())) {
                return appender;
            }
        }
        return null;
    }

    @Override
    public boolean isAttached(IAppender appender) {
        if (appender == null) {
            return false;
        }
        for (IAppender a : appenderList) {
            if (a == appender) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeAppender(IAppender appender) {
        removeAppender(appender.getName());
    }

    @Override
    public void removeAppender(String name) {
        if (name == null) {
            return;
        }
        for (IAppender a : appenderList) {
            if (name.equals((a).getName())) {
                appenderList.remove(a);
                break;
            }
        }
    }
}
