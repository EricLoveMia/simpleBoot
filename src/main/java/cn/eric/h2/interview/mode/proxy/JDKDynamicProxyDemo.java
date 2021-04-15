package cn.eric.h2.interview.mode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName JDKDynamicProxyDemo
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/8
 * @Version V1.0
 **/
public class JDKDynamicProxyDemo {
    interface Iservice {
        void sayHello();
    }

    static class Service implements Iservice {
        @Override
        public void sayHello() {
            System.out.println("say hello~~~~~");
        }
    }

    static class JDKInvocationHandler implements InvocationHandler {
        //被代理对象
        private Object object;

        public JDKInvocationHandler(Object object) {
            this.object = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before--say hello");
            Object invoke = method.invoke(object, args);
            System.out.println("after--say hello");
            return invoke;
        }

        /**
         * 生成代理类对象
         *
         * @return
         */
        public Object newProxyInstance() {
            return Proxy.newProxyInstance(
                    object.getClass().getClassLoader(),
                    object.getClass().getInterfaces(),
                    this);
        }
    }

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        JDKInvocationHandler jdkInvocationHandler = new JDKInvocationHandler(new Service());
        Iservice service = (Iservice) jdkInvocationHandler.newProxyInstance();
        service.sayHello();
    }
}

