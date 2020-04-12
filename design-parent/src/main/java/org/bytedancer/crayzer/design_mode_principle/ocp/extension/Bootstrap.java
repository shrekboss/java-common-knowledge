package org.bytedancer.crayzer.design_mode_principle.ocp.extension;

import org.bytedancer.crayzer.design_mode_principle.ocp.extension.vo.ApiStatInfo;

public class Bootstrap {
    public static void main(String[] args) {
        ApiStatInfo apiStatInfo = new ApiStatInfo();
        // ...省略设置apiStatInfo数据值的代码
        apiStatInfo.setTimeoutCount(100);
        ApplicationContext.getInstance().getAlert().check(apiStatInfo);
    }
}
