package org.bytedancer.crayzer.common_dev_error.coding.oom.usernameautocomplete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}