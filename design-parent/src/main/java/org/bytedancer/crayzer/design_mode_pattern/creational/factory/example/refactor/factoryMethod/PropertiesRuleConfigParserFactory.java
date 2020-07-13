package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.refactor.factoryMethod;

import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.IRuleConfigParser;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.PropertiesRuleConfigParser;

public class PropertiesRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new PropertiesRuleConfigParser();
    }
}
