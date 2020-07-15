package org.bytedancer.crayzer.design_mode_pattern.behavioural.visitor.refactor.v3;

public class WordExtractor implements Extractor {
    @Override
    public void extract2txt(ResourceFile resourceFile) {
        System.out.println("Extract WORD.");
    }
}
