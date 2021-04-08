package cn.eric.h2.interview.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CGLibDemo
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/8
 * @Version V1.0
 **/
public class CGLibDemo {
    static class Service {
        public void sayHello() {
            System.out.println("say hello~~~~~");
        }
    }

    static class SimpleInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("before--" + method.getName());
            Object result = methodProxy.invokeSuper(o, objects);
            System.out.println("after--" + method.getName());
            return result;
        }
    }

    private static <T> T getProxy(Class<T> tClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new SimpleInterceptor());
        return (T) enhancer.create();
    }

    public static void main(String[] args) {
        Service service = getProxy(Service.class);
        service.sayHello();
    }
}
