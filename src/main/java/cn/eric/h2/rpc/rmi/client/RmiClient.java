package cn.eric.h2.rpc.rmi.client;

import cn.eric.h2.rpc.rmi.server.HelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @ClassName RmiClient
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/1
 * @Version V1.0
 **/
public class RmiClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        HelloService helloService = (HelloService) Naming.lookup("rmi://localhost:8801/helloService");
        //调用远程方法
        System.out.println("RMI服务器返回的结果是 " + helloService.sayHello("mia"));
    }
}
