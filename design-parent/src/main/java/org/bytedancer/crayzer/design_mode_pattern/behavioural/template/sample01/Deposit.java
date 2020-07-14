package org.bytedancer.crayzer.design_mode_pattern.behavioural.template.sample01;

public class Deposit extends BankTemplateMethod {

    @Override
    public void transact() {
        System.out.println("存款");
    }
}
