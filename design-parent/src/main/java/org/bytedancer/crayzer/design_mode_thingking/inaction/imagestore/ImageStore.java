package org.bytedancer.crayzer.design_mode_thingking.inaction.imagestore;


import org.bytedancer.crayzer.design_mode_thingking.inaction.imagestore.pojo.Image;

public interface ImageStore {
    String upload(Image image, String buctetName);
    Image download(String url);
}
