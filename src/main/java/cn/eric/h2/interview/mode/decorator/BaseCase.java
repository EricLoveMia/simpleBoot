package cn.eric.h2.interview.mode.decorator;

import java.math.BigDecimal;

/**
 * @ClassName BaseCase
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class BaseCase extends Cake {
    @Override
    public String getCakeMsg() {
        return "我是一个8英寸的普通奶油蛋糕";
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal("68");
    }
}
