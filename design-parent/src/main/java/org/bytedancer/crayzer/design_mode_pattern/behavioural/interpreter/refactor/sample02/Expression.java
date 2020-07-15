package org.bytedancer.crayzer.design_mode_pattern.behavioural.interpreter.refactor.sample02;

import java.util.Map;

public interface Expression {
    boolean interpret(Map<String, Long> stats);
}
