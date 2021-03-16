package org.bytedancer.crayzer.projects.mylog.appender;

import org.bytedancer.crayzer.projects.mylog.event.LoggingEvent;
import org.bytedancer.crayzer.projects.mylog.filter.Filter;
import org.bytedancer.crayzer.projects.mylog.layout.Layout;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author yizhe.chen
 */
public abstract class AppenderBase implements IAppender {

    protected Layout layout;
    protected List<Filter> filterList;
    protected String encoding = Charset.defaultCharset().name();
    protected String name;

    protected Object lock = new Object();

    @Override
    public void append(LoggingEvent e) {
        if (filterList != null) {
            for (Filter filter : filterList) {
                if (!filter.doFilter(e)) {
                    return;
                }
            }
        }
        String body = layout.doLayout(e);
        synchronized (lock) {
            doAppend(body);
        }
    }

    protected abstract void doAppend(String body);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Layout getLayout() {
        return layout;
    }

    public List<Filter> getFilterList() {
        return filterList;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}