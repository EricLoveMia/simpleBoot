package cn.eric.h2.interview.mode.singleton.hungry;

/**
 * @ClassName HungrySingleton
 * @Description: 饿汉式
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class HungrySingleton {
    private static final HungrySingleton singleton = new HungrySingleton();

    HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return singleton;
    }
}
