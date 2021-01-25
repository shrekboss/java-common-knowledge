package org.bytedancer.crayzer.projects.mylog.layout.pattern;

import org.bytedancer.crayzer.projects.mylog.exception.ConfigException;
import org.bytedancer.crayzer.projects.mylog.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析日志输出规则
 *
 * @author yizhe.chen
 */
public class PatternParser {

    enum ParserState {
        /**
         *
         */
        LITERAL_STATE,
        KEYWORD_STATE
    }

    public static final char PERCENT_CHAR = '%';

    private String pattern;

    public static final Map<String, Class> DEFAULT_CONVERTER_MAP = new HashMap<>();

    public PatternParser(String pattern) {
        this.pattern = pattern;
    }

    static {
        //put converters to defaultConverterMap
        DEFAULT_CONVERTER_MAP.put("d", DateConverter.class);
        DEFAULT_CONVERTER_MAP.put("t", ThreadConverter.class);
        DEFAULT_CONVERTER_MAP.put("m", MessageConverter.class);
        DEFAULT_CONVERTER_MAP.put("p", LevelConverter.class);
        DEFAULT_CONVERTER_MAP.put("c", LoggerConverter.class);
        DEFAULT_CONVERTER_MAP.put("n", LineSeparatorConverter.class);
    }

    public List<Node> parse() {
        List<Node> nodes = new ArrayList<>();
        int i = 0;
        char c;
        ParserState state = ParserState.LITERAL_STATE;
        StringBuilder literalBuf = new StringBuilder();
        int patternLength = pattern.length();
        while (i != patternLength - 1) {
            c = pattern.charAt(i++);
            switch (state) {
                case LITERAL_STATE:
                    if (c == PERCENT_CHAR) {
                        if (literalBuf.length() > 0) {
                            Node node = new Node(Node.LITERAL, literalBuf.toString());
                            nodes.add(node);
                            literalBuf.setLength(0);
                        }
                        state = ParserState.KEYWORD_STATE;
                    }
                    literalBuf.append(c);
                    break;
                case KEYWORD_STATE:
                    if (!Character.isJavaIdentifierPart(c)) {

                        KeywordNode node = new KeywordNode(literalBuf.toString());
                        nodes.add(node);
                        state = ParserState.LITERAL_STATE;
                        literalBuf.setLength(0);
                    }
                    literalBuf.append(c);
                    break;
                default:
            }
        }

        compileNode(nodes);
        return nodes;
    }

    private void compileNode(List<Node> nodes) {
        for (Node n : nodes) {
            Converter converter;
            if (n instanceof KeywordNode) {
                String keyword = ((KeywordNode) n).getKeyword();
                Class clazz = DEFAULT_CONVERTER_MAP.get(keyword);
                if (clazz == null) {
                    throw new ConfigException("pattern[%" + keyword + "] illegal!");
                }
                try {
                    converter = (Converter) ReflectionUtils.newInstance(clazz);
                } catch (Exception e) {
                    throw new ConfigException(e);
                }
            } else {
                converter = new LiteralConverter(n.getValue());
            }
            n.converter = converter;
        }
    }

    private Node newHead(Node head, Node node) {
        if (head == null) {
            head = node;
        } else {

            head.next = node;
        }
        return head;
    }

    public static void main(String[] args) {
        System.out.println(Character.isJavaIdentifierPart(' '));
    }
}
