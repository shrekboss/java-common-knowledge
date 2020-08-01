package org.bytedancer.crayzer.common_dev_error.coding.exception.finallyissue;

public class TestResource implements AutoCloseable {

    public void read() throws Exception {
        throw new Exception("read error");
    }

    @Override
    public void close() throws Exception {
        throw new Exception("close error");
    }
}
