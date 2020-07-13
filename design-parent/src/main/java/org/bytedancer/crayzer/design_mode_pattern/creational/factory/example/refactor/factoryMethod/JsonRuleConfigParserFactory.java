package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.refactor.factoryMethod;

import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.IRuleConfigParser;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.JsonRuleConfigParser;

public class JsonRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new JsonRuleConfigParser();
    }
}