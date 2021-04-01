package cn.eric.h2.rpc.rmi.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @ClassName ServiceMain
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/1
 * @Version V1.0
 **/
public class ServiceMain {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        LocateRegistry.createRegistry(8801);

        HelloService helloService = new HelloServiceImpl();
        Naming.bind("rmi://localhost:8801/helloService", helloService);
        System.out.println("ServiceMain provide RPC service now.");
    }
}
