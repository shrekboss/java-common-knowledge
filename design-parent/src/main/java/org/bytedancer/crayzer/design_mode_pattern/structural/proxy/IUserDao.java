package org.bytedancer.crayzer.design_mode_pattern.structural.proxy;

public interface IUserDao {
    void save();
    void edit();
    void delete();

    void saveOrUpdate();
}
