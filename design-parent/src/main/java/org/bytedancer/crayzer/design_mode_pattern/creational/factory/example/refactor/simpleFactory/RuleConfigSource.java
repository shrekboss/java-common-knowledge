package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.refactor.simpleFactory;


import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.InvalidRuleConfigException;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.IRuleConfigParser;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.original.RuleConfig;

public class RuleConfigSource {
    public RuleConfig load(String ruleConfigFilePath) {
        String ruleConfigFileExtension = getFileExtension(ruleConfigFilePath);
        IRuleConfigParser parser = RuleConfigParserFactory.createParser(ruleConfigFileExtension);
        if (parser == null) {
            throw new InvalidRuleConfigException(
                    "Rule config file format is not supported: " + ruleConfigFilePath);
        }

        String configText = "";
        //从ruleConfigFilePath文件中读取配置文本到configText中
        RuleConfig ruleConfig = parser.parse(configText);
        return ruleConfig;
    }

    private String getFileExtension(String filePath) {
        //...解析文件名获取扩展名，比如rule.json，返回json
        return "json";
    }
}
