package cn.eric.h2.interview.mode.decorator;

import java.math.BigDecimal;

/**
 * @ClassName CakeAddGrapeDecorator
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class CakeAddGrapeDecorator extends CakeDecorator {

    public CakeAddGrapeDecorator(Cake cake) {
        super(cake);
    }

    @Override
    public String getCakeMsg() {
        return super.getCakeMsg() + " +1个葡萄";
    }

    @Override
    public BigDecimal getPrice() {
        return super.getPrice().add(new BigDecimal("5"));
    }
}
