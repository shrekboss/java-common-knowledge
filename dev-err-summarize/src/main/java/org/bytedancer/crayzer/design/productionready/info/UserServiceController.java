package org.bytedancer.crayzer.devmisuse.design.productionready.info;

import lombok.extern.slf4j.Slf4j;
import org.bytedancer.crayzer.design.productionready.info.ThreadPoolProvider;
import org.bytedancer.crayzer.design.productionready.info.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("user")
public class UserServiceController {
    @GetMapping
    public User getUser(@RequestParam("userId") long id) {
        if (ThreadLocalRandom.current().nextInt() % 2 == 0)
            return new User(id, "name" + id);
        else
            throw new RuntimeException("error");
    }

    @GetMapping("slowTask")
    public void slowTask() {
        ThreadPoolProvider.getDemoThreadPool().execute(() -> {
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
            }
        });
    }
}