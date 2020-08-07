package org.bytedancer.crayzer.design.apidesgin.apiasyncsyncmode;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SyncQueryUploadTaskResponse {
    private final String taskId;
    private String downloadUrl;
    private String thumbnailDownloadUrl;
}
