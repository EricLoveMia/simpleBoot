package cn.eric.h2.socket.nio_multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ServerNIOSingle
 * @Description: nio 多线程 reactor 响应式编程
 *
 * @Author YCKJ2725
 * @Date 2020/6/5
 * @Version V1.0
 **/
public class ServerNIOMulti {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress("127.0.0.1",8888));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                handle(next);
            }
        }
    }

    private static void handle(SelectionKey key) {
        if(key.isAcceptable()){
            try {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel accept = ssc.accept();
                accept.configureBlocking(false);

                accept.register(key.selector(),SelectionKey.OP_READ);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }else if (key.isReadable()){
            SocketChannel sc = null;
            try {
                sc = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                byteBuffer.clear();
                int len = sc.read(byteBuffer);

                if(len != -1){
                    System.out.println(new String(byteBuffer.array(),0,len));
                }
                ByteBuffer byteBufferWrite = ByteBuffer.wrap("HELLO".getBytes());
                sc.write(byteBufferWrite);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(sc != null){
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
