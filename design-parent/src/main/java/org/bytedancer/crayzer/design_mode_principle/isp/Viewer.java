package org.bytedancer.crayzer.design_mode_principle.isp;

import java.util.Map;

public interface Viewer {
    String outputInPlainText();
    Map<String, String> output();
}
