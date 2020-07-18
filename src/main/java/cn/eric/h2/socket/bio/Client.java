package cn.eric.h2.socket.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName Client
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/6/5
 * @Version V1.0
 **/
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1",8888));
        socket.getOutputStream().write("hello".getBytes());
        byte[] bytes = new byte[1024];
        socket.getInputStream().read(bytes);
        System.out.println("back:" + new String(bytes));
    }
}
