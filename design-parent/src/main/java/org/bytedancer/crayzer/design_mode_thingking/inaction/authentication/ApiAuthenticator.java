package org.bytedancer.crayzer.design_mode_thingking.inaction.authentication;

public interface ApiAuthenticator {
    void auth(String url);

    void auth(ApiRequest apiRequest);
}
