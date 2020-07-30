package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.feignandribbontimeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/feignandribbon")
@Slf4j
public class FeignAndRibbonController {

    @Autowired
    private Client client;

    /**
     * org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration
     * public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
     * public static final int DEFAULT_READ_TIMEOUT = 1000;
     * }
     * # 结论一，默认情况下 Feign 的读取超时是 1 秒，如此短的读取超时算是坑点一。
     * 执行耗时：1447ms 错误：Read timed out executing POST http://clientsdk/feignandribbon/server
     * <p>
     * # 结论二，如果要配置 Feign 的读取超时，就必须同时配置连接超时，才能生效。
     * org.springframework.cloud.openfeign.FeignClientFactoryBean#configureUsingProperties(org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration, feign.Feign.Builder)
     * feign.client.config.default.readTimeout=3000
     * feign.client.config.default.connectTimeout=3000
     * <p>
     * 结论三，单独的超时可以覆盖全局超时，这符合预期
     * feign.client.config.default.readTimeout=3000
     * feign.client.config.default.connectTimeout=3000
     * feign.client.config.clientsdk.readTimeout=2000
     * feign.client.config.clientsdk.connectTimeout=2000
     * <p>
     * 结论四，除了可以配置 Feign，也可以配置 Ribbon 组件的参数来修改两个超时时间。
     * 但是参数首字母要大写，和 Feign 的配置不同。
     * <p>
     * ribbon.ReadTimeout=4000
     * ribbon.ConnectTimeout=4000
     * <p>
     * 结论五，同时配置 Feign 和 Ribbon 的超时，以 Feign 为准
     * <p>
     * clientsdk.ribbon.listOfServers=localhost:45678
     * feign.client.config.default.readTimeout=3000
     * feign.client.config.default.connectTimeout=3000
     * ribbon.ReadTimeout=4000
     * ribbon.ConnectTimeout=4000
     */
    @GetMapping("/client")
    public void timeout() {
        long begin = System.currentTimeMillis();
        try {
            client.server();
        } catch (Exception ex) {
            log.warn("执行耗时：{}ms 错误：{}", System.currentTimeMillis() - begin, ex.getMessage());
        }
    }

    @PostMapping("/server")
    public void server() throws InterruptedException {
        TimeUnit.MINUTES.sleep(10);
    }
}
