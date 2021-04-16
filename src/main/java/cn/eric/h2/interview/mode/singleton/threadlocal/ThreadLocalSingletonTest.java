package cn.eric.h2.interview.mode.singleton.threadlocal;

/**
 * @ClassName ThreadLocalSingletonTest
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class ThreadLocalSingletonTest {
    public static void main(String[] args) {
        System.out.println(ThreadLocalSingleton.getInstance());
        System.out.println(ThreadLocalSingleton.getInstance());

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalSingleton singleton = ThreadLocalSingleton.getInstance();
                System.out.println(Thread.currentThread().getName() + ":" + singleton);
            }
        });
        t1.start();
    }
}
