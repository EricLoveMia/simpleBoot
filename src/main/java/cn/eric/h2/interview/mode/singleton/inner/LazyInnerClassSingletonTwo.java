package cn.eric.h2.interview.mode.singleton.inner;

/**
 * @ClassName LazyInnerClassSingleton
 * @Description: 利用了内部类的特性，LazyHolder里面的逻辑需要等到外面方法调用时才执行
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class LazyInnerClassSingletonTwo {

    private LazyInnerClassSingletonTwo() {
        // 防止反射攻击
        if (null != LazyHolder.LAZY) {
            throw new RuntimeException("不允许构造多个实例");
        }
    }

    public static final LazyInnerClassSingletonTwo getInstance() {
        return LazyHolder.LAZY;
    }

    /**
     * 静态内部类是jdk层面保证线程安全的
     * 这种写法看起来很完美，没有加锁，也保证了懒加载，但是这种单例模式也有问题，那就是可以被反射或者序列化破坏单例
     **/
    private static class LazyHolder {
        private static final LazyInnerClassSingletonTwo LAZY = new LazyInnerClassSingletonTwo();
    }
}
