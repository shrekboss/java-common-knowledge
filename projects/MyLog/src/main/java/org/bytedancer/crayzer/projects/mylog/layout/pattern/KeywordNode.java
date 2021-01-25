package org.bytedancer.crayzer.projects.mylog.layout.pattern;

public class KeywordNode extends Node {

    public KeywordNode(String value) {
        super(KEYWORD, value);
    }

    public KeywordNode() {
    }

    public String getKeyword() {
        return value.substring(1);
    }
}
