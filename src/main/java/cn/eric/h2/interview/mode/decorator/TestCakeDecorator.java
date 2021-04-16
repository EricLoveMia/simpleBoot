package cn.eric.h2.interview.mode.decorator;

/**
 * @ClassName TestCakeDecorator
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class TestCakeDecorator {

    public static void main(String[] args) {
        Cake cake = new BaseCase();
        System.out.println(cake.getCakeMsg() + ",价格：" + cake.getPrice());

        cake = new CakeAddGrapeDecorator(cake);
        System.out.println(cake.getCakeMsg() + ",价格：" + cake.getPrice());

        //加一个葡萄
        cake = new CakeAddGrapeDecorator(cake);
        System.out.println(cake.getCakeMsg() + ",价格：" + cake.getPrice());
        //再加一个芒果
        cake = new CakeAddMangoDecorator(cake);
        System.out.println(cake.getCakeMsg() + ",价格：" + cake.getPrice());
    }
}
