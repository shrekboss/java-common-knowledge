package org.bytedancer.crayzer.projects.mylog.config;

import org.bytedancer.crayzer.projects.mylog.ILifeCycle;

/**
 * @author yizhe.chen
 */
public interface IConfigurator extends ILifeCycle {

    void doConfigure();
}
