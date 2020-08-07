package org.bytedancer.crayzer.devmisuse.safety.dataandcode.sqlinject;

import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String name;
    private String password;
}
