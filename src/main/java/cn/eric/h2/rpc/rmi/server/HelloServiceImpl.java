package cn.eric.h2.rpc.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @ClassName HelloServiceImpl
 * @Description: 必须实现UnicastRemoteObject 该类定义了服务调用方与服务提供方对象实例，并简历一对一的连接
 * @Author YCKJ2725
 * @Date 2021/4/1
 * @Version V1.0
 **/
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    private static final long serialVersionUID = -7392394859973559437L;

    public HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String someOne) throws RemoteException {
        return "Hello -- " + someOne;
    }
}
