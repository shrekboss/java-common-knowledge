package org.bytedancer.crayzer.design_mode_pattern.behavioural.chainOfResponsibility.sample;

public class PoliticalWordFilter implements SensitiveWordFilter {
    @Override
    public boolean doFilter(Content content) {
        boolean legal = true;
        //...
        return legal;
    }
}
