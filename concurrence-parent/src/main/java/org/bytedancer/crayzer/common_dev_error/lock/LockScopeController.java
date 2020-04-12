package org.crayzer.common.dev.err.threadsafe.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("lockscope")
@Slf4j
public class LockScopeController {

    @GetMapping("wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        WrongData.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new WrongData().wrong());
        return WrongData.getCounter();
    }

    @GetMapping("right")
    public int right(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        RightData.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new RightData().right());
        return RightData.getCounter();
    }
}
