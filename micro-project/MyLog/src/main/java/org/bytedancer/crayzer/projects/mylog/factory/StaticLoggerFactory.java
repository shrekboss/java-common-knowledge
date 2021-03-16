package org.bytedancer.crayzer.projects.mylog.factory;

import org.bytedancer.crayzer.projects.mylog.DefaultLogger;
import org.bytedancer.crayzer.projects.mylog.ILogger;
import org.bytedancer.crayzer.projects.mylog.context.ContextInitializer;
import org.bytedancer.crayzer.projects.mylog.context.LoggerContext;

/**
 * @author yizhe.chen
 */
public class StaticLoggerFactory implements ILoggerFactory {

    private static final String SEPARATOR = ".";

    private LoggerContext loggerContext;

    public StaticLoggerFactory() {
        //构造 StaticLoggerFactory 时，直接调用配置解析的方法，并获取loggerContext
        ContextInitializer.autoConfig();
        loggerContext = ContextInitializer.getDefaultLoggerContext();
    }

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    @Override
    public ILogger getLogger(String name) {
        ILogger logger = loggerContext.getLoggerCache().get(name);
        if (logger == null) {
            logger = newLogger(name);
        }
        return logger;
    }

    /**
     * 创建 ILogger 对象
     * 匹配 logger name，拆分类名后和已创建（包括配置的）的 ILogger 进行匹配
     * 比如当前 name 为 com.aaa.bbb.ccc.XXService，那么 name 为 com/com.aaa/com.aaa.bbb/com.aaa.bbb.ccc
     * 的 logger 都可以作为 parent logger，不过这里需要顺序拆分，优先匹配“最近的”
     * 在这个例子里就会优先匹配 com.aaa.bbb.ccc 这个 logger，作为自己的 parent
     * <p>
     * 如果没有任何一个 logger 匹配，那么就使用 root logger 作为自己的 parent
     *
     * @param name Logger name
     */
    @Override
    public ILogger newLogger(String name) {
        DefaultLogger logger = new DefaultLogger();
        logger.setName(name);
        ILogger parent = null;
        // 拆分包名，向上查找parent logger
        for (int i = name.lastIndexOf(SEPARATOR); i >= 0; i = name.lastIndexOf(SEPARATOR, i - 1)) {
            String parentName = name.substring(0, i);
            parent = loggerContext.getLoggerCache().get(parentName);

            if (parent != null) {
                break;
            }
        }

        if (parent == null) {
            parent = loggerContext.getRoot();
        }

        logger.setParent(parent);
        logger.setLoggerContext(loggerContext);

        return logger;
    }
}
