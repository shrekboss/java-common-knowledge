package org.bytedancer.crayzer.common_dev_error.coding.srpingpart1.aopmetrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //自动进行监控
@RequestMapping("metricstest")

// todo 修改点
@Metrics(logParameters = false, logReturn = false)
// @Metrics

// 将MetricsAspect这个Bean的优先级设置为最高 值越大优先级反而越低
// @Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsController {
    @Autowired
    private UserService userService;

    @GetMapping("transaction")
    public int transaction(@RequestParam("name") String name) {
        try {
            userService.createUser(new UserEntity(name));
        } catch (Exception ex) {
            log.error("create user failed because {}", ex.getMessage());
        }
        return userService.getUserCount(name);
    }
}
