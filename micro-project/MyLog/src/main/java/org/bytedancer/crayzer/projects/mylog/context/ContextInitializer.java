package org.bytedancer.crayzer.projects.mylog.context;

import org.bytedancer.crayzer.projects.mylog.config.IConfigurator;
import org.bytedancer.crayzer.projects.mylog.config.XMLConfigurator;
import org.bytedancer.crayzer.projects.mylog.config.YAMLConfigurator;

import java.net.URL;

/**
 * @author yizhe.chen
 */
public class ContextInitializer {

    private static final String SUFFIX_XML = "xml";
    private static final String SUFFIX_YML = "yml";

    //默认使用xml配置文件
    public static final String DEFAULT_CONFIG_FILE = "mylog.xml";
    public static final String YAML_FILE = "mylog.yml";
    private static final LoggerContext DEFAULT_LOGGER_CONTEXT = new LoggerContext();

    /**
     * 初始化上下文
     */
    public static void autoConfig() {
        URL url = getConfigURL();
        if (url == null) {
            System.err.println("config[ mylog.xml or mylog.yml ] file not found!");
            return;
        }

        String urlString = url.toString();
        IConfigurator configurator = null;

        if (urlString.endsWith(SUFFIX_XML)) {
            configurator = new XMLConfigurator(url, DEFAULT_LOGGER_CONTEXT);
        }
        if (urlString.endsWith(SUFFIX_YML)) {
            configurator = new YAMLConfigurator(url, DEFAULT_LOGGER_CONTEXT);
        }
        configurator.doConfigure();
    }

    private static URL getConfigURL() {
        ClassLoader classLoader = ContextInitializer.class.getClassLoader();
        URL url = classLoader.getResource(DEFAULT_CONFIG_FILE);
        if (url != null) {
            return url;
        }

        return classLoader.getResource(YAML_FILE);
    }

    /**
     * 获取全局默认的LoggerContext
     */
    public static LoggerContext getDefaultLoggerContext() {
        return DEFAULT_LOGGER_CONTEXT;
    }
}
