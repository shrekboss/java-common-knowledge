package org.bytedancer.crayzer.projects.mylog.exception;

/**
 * @author yizhe.chen
 */
public class ConfigException extends RuntimeException {
    public ConfigException(Exception e) {
        super(e);
    }

    public ConfigException(String message) {
        super(message);
    }
}
