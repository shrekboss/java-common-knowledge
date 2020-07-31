package org.bytedancer.crayzer.common_dev_error.coding.connPool.twotimeoutconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
