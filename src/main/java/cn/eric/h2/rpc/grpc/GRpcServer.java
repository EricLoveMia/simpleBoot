package cn.eric.h2.rpc.grpc;

import io.grpc.Server;

import java.rmi.RemoteException;

/**
 * @ClassName GRpcServer
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/1
 * @Version V1.0
 **/
public class GRpcServer {

    /**
     * 服务端口
     */
    private int port = 50051;

    /**
     * 服务端服务管理器
     */
    private Server server;

    private void start() throws RemoteException {
        // 初始化并启动服务
//        server = ServerBuilder.forPort(port)
//                .addService(new HelloServiceImpl())
//                .build().start();
    }

}
