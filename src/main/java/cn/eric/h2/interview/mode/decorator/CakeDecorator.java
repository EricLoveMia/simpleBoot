package cn.eric.h2.interview.mode.decorator;

import java.math.BigDecimal;

/**
 * @ClassName CakeDecorator
 * @Description: 蛋糕装饰器抽象类
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public abstract class CakeDecorator extends Cake {

    private Cake cake;

    public CakeDecorator(Cake cake) {
        this.cake = cake;
    }

    @Override
    public String getCakeMsg() {
        return this.cake.getCakeMsg();
    }

    @Override
    public BigDecimal getPrice() {
        return this.cake.getPrice();
    }

}
