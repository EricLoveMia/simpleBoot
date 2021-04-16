package cn.eric.h2.interview.mode.singleton.inner;

import java.io.Serializable;

/**
 * @ClassName LazyInnerClassSingleton
 * @Description: 利用了内部类的特性，LazyHolder里面的逻辑需要等到外面方法调用时才执行
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class LazyInnerClassSingletonSerial implements Serializable {

    private static final long serialVersionUID = 8067801568319855229L;

    private LazyInnerClassSingletonSerial() {
        // 防止反射攻击
        if (null != LazyHolder.LAZY) {
            throw new RuntimeException("不允许构造多个实例");
        }
    }

    /**
     * 防止序列化破坏单例
     * 这是因为JDK源码中会检验一个类中是否存在一个readResolve()方法，如果存在，则会放弃通过序列化产生的对象，而返回原本的对象，
     * 也就是说，在校验是否存在readResolve()方法前产生了一个对象，只不过这个对象会在发现类中存在readResolve()方法后丢掉，
     * 然后返回原本的单例对象，保证了单例的唯一性，这种写法虽然保证了单例唯一，但是过程中类也是会被实例化两次，
     * 假如创建对象的频率增大，就意味着内存分配的开销也随之增大
     **/
    private Object readResolve() {
        return LazyHolder.LAZY;
    }

    public static final LazyInnerClassSingletonSerial getInstance() {
        return LazyHolder.LAZY;
    }

    /**
     * 静态内部类是jdk层面保证线程安全的
     * 这种写法看起来很完美，没有加锁，也保证了懒加载，但是这种单例模式也有问题，那就是可以被反射或者序列化破坏单例
     **/
    private static class LazyHolder {
        private static final LazyInnerClassSingletonSerial LAZY = new LazyInnerClassSingletonSerial();
    }
}
