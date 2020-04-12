package org.bytedancer.crayzer.design_mode_thingking.inaction.authentication;

public interface CredentialStorage {
    String getPasswordByAppId(String appId);
}
