package cn.eric.h2.rpc.grpc;

/**
 * @author eric
 */
public interface HelloService {

    /**
     * @MethodName: sayHello
     * @Description: rmi接口方法定义必须显式声明抛出RemoteException异常
     * @Param: [someOne]
     * @Return: java.lang.String
     * @Author: YCKJ2725
     * @Date: 2021/4/1 8:55
     **/
    String sayHello(String someOne);
}
