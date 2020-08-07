package org.bytedancer.crayzer.devmisuse.coding.httpinvoke.clientreadtimeout;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("clientreadtimeout")
@Slf4j
public class ClientReadTimeoutController {

    private String getResponse(String url, int connectTimeout, int readTimeout) throws IOException {
        return Request.Get("http://localhost:45678/clientreadtimeout" + url)
                .connectTimeout(connectTimeout)
                .socketTimeout(readTimeout)
                .execute()
                .returnContent()
                .asString();
    }

    /**
     * java.net.SocketTimeoutException: Read timed out
     * 第一个误区：认为出现了读取超时，服务端的执行就会中断。
     * 第二个误区：认为读取超时只是 Socket 网络层面的概念，是数据传输的最长耗时，故将其配置得非常
     * 短，比如 100 毫秒。
     * 第三个误区：认为超时时间越长任务接口成功率就越高，将读取超时参数配置得太长。
     */
    @GetMapping("client")
    public String client() throws IOException {
        log.info("client1 called");
        // 服务端5s超时，客户端读取超时2s
        return getResponse("/server?timeout=5000", 1000, 2000);
    }

    @GetMapping("/server")
    public void server(@RequestParam("timeout") int timeout) throws InterruptedException {
        log.info("server called");
        TimeUnit.MILLISECONDS.sleep(timeout);
        log.info("Done");
    }
}
