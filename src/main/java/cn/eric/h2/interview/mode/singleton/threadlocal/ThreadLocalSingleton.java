package cn.eric.h2.interview.mode.singleton.threadlocal;

/**
 * @ClassName ThreadLocalSingleton
 * @Description: ThreadLocal不能保证其创建的对象是全局唯一，但是能保证在单个线程中是唯一的，天生的线程安全
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class ThreadLocalSingleton {
    private ThreadLocalSingleton() {
    }

    private static final ThreadLocal<ThreadLocalSingleton> singleton
            = ThreadLocal.withInitial(() -> new ThreadLocalSingleton());

    public static ThreadLocalSingleton getInstance() {
        return singleton.get();
    }
}
