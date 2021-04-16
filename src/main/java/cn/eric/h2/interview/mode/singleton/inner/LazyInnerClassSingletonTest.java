package cn.eric.h2.interview.mode.singleton.inner;

import java.lang.reflect.Constructor;

/**
 * @ClassName LazyInnerClassSingletonTest
 * @Description: 反射破坏单例的例子
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class LazyInnerClassSingletonTest {

    public static void main(String[] args) throws Exception {
        Class<?> clazz = LazyInnerClassSingleton.class;
        Constructor<?> constructor = clazz.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        Object singleton = LazyInnerClassSingleton.getInstance();
        System.out.println(instance == singleton);

        // 防止反射攻击后
        Class<?> clazzTwo = LazyInnerClassSingletonTwo.class;
        Constructor<?> constructorTwo = clazzTwo.getDeclaredConstructor(null);
        constructorTwo.setAccessible(true);
        Object instanceTwo = constructorTwo.newInstance();
        Object singletonTwo = LazyInnerClassSingletonTwo.getInstance();
        System.out.println(instanceTwo == singletonTwo);
    }
}
