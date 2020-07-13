package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.ruleFacotry.refactor.factoryMethod;

import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.ruleFacotry.IRuleConfigParser;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.ruleFacotry.XmlRuleConfigParser;

public class XmlRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new XmlRuleConfigParser();
    }
}
