package cn.eric.h2.interview.mode.singleton.hungry;

/**
 * @ClassName HungryStaticSingleton
 * @Description: 用静态代码块的方式实现饿汉式单例
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class HungryStaticSingleton {
    private static final HungryStaticSingleton singleton;

    static {
        singleton = new HungryStaticSingleton();
    }

    private HungryStaticSingleton() {
    }

    public static HungryStaticSingleton getInstance() {
        return singleton;
    }
}
