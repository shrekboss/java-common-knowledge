package org.bytedancer.crayzer.common_dev_error.safety.sensitivedata.storepassword;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserData {
    @Id
    private Long id;
    private String name;
    private String salt;
    private String password;
}
