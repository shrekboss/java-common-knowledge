package org.bytedancer.crayzer.projects.mylog.context;

import lombok.Data;
import org.bytedancer.crayzer.projects.mylog.ILogger;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个全局的上下文对象
 *
 * @author yizhe.chen
 */
@Data
public class LoggerContext {

    private ILogger root;

    /**
     * logger缓存，存放解析配置文件后生成的logger对象，以及通过程序手动创建的logger对象
     */
    private Map<String, ILogger> loggerCache = new HashMap<>();

    public void addLogger(String name, ILogger logger) {
        loggerCache.put(name, logger);
    }

    public void addLogger(ILogger logger) {
        loggerCache.put(logger.getName(), logger);
    }
}
