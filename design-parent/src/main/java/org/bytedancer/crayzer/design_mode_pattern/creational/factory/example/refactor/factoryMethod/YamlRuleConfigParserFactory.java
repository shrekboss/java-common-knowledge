package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.refactor.factoryMethod;

import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.IRuleConfigParser;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.YamlRuleConfigParser;

public class YamlRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new YamlRuleConfigParser();
    }
}
