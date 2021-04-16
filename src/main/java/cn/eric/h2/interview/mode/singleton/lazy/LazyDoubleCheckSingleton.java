package cn.eric.h2.interview.mode.singleton.lazy;

/**
 * @ClassName LazyDoubleCheckSingleton
 * @Description: 双重检查锁（double-checked locking）单例
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class LazyDoubleCheckSingleton {

    private static LazyDoubleCheckSingleton singleton = null;

    private LazyDoubleCheckSingleton() {
    }

    /**
     * 写法将同步放在了方法里面的第一个非空判断之后，这样可以确保对象不为空的时候不会被阻塞
     **/
    public static LazyDoubleCheckSingleton getInstance() {
        if (null == singleton) {
            synchronized (LazyDoubleCheckSingleton.class) {
                if (null == singleton) {
                    singleton = new LazyDoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }
}
