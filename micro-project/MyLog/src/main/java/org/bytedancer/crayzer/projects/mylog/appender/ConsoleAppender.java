package org.bytedancer.crayzer.projects.mylog.appender;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yizhe.chen
 */
public class ConsoleAppender extends AppenderBase {

    private OutputStream out = System.out;
    private OutputStream out_err = System.err;

    @Override
    protected void doAppend(String body) {
        try {
            out.write(body.getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}