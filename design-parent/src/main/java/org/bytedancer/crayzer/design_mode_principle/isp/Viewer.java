package org.bytedancer.crayzer.design_mode_principle.isp;

import java.util.Map;

/**
 * class_name: Viewer
 * package: org.bytedancer.crayzer.design_mode_principle.isp
 * describe: 视图查看接口
 * @author yixiu
 **/
public interface Viewer {
    String outputInPlainText();
    Map<String, String> output();
}
