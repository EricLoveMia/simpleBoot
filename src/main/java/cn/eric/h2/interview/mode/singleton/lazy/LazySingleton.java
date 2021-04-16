package cn.eric.h2.interview.mode.singleton.lazy;

/**
 * @ClassName LazySingleton
 * @Description: 懒汉式
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class LazySingleton {
    private static LazySingleton singleton = null;

    private LazySingleton() {
    }

    /**
     * 最简单的一种懒汉式单例写法，但是存在线程安全问题，多线程情况下会有一定几率返回多个单例对象，这明显违背了单例对象原则
     **/
    public static LazySingleton getInstance() {
        if (null == singleton) {
            return new LazySingleton();
        }
        return singleton;
    }

    /**
     * 用 synchronized 加锁，在线程数量比较多情况下，如果CPU分配压力上升，会导致大批量线程出现阻塞，从而导致程序运行性能大幅下降
     **/
    public synchronized static LazySingleton getInstanceSyn() {
        if (null == singleton) {
            return new LazySingleton();
        }
        return singleton;
    }
}
