package cn.eric.h2.interview.mode.decorator;

import java.math.BigDecimal;

/**
 * @ClassName CakeAddMangoDecorator
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class CakeAddMangoDecorator extends CakeDecorator {

    public CakeAddMangoDecorator(Cake cake) {
        super(cake);
    }

    @Override
    public String getCakeMsg() {
        return super.getCakeMsg() + " +1个芒果";
    }

    @Override
    public BigDecimal getPrice() {
        return super.getPrice().add(new BigDecimal("4"));
    }

}
