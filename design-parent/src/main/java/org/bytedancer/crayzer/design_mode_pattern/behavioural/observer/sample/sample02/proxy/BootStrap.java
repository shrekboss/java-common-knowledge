package org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.sample.sample02.proxy;

import org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.sample.sample02.Observer;
import org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.sample.sample02.SubjectEventType;

public class BootStrap {
    public static void main(String[] args) {
        Observer observer = new Observer();
        ISubject subject = (ISubject) new SubjectProxy(new SubjectImpl(), observer, SubjectEventType.ON_ADD).getInstance();

        subject.add();
    }
}
