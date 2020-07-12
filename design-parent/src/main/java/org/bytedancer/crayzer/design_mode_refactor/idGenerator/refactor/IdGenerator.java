package org.bytedancer.crayzer.design_mode_refactor.idGenerator.refactor;

public interface IdGenerator {
    String generate() throws IdGenerationFailureException;
}
