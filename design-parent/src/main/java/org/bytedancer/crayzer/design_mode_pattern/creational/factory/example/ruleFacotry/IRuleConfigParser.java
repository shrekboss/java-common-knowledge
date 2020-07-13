package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.ruleFacotry;

public interface IRuleConfigParser {
    RuleConfig parse(String configText);
}
