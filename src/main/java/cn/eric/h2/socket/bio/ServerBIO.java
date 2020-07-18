package cn.eric.h2.socket.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName ServerBIO
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/6/5
 * @Version V1.0
 **/
public class ServerBIO {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1",8888));
        while (true){
            Socket socket = serverSocket.accept();

            new Thread(() -> {
                hanlder(socket);
            }).start();
        }
    }

    static void hanlder(Socket socket)  {
        try {
            byte[] bytes = new byte[1024];
            int len = socket.getInputStream().read(bytes);
            System.out.println(new String(bytes,0,len));
            socket.getOutputStream().write(bytes);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
