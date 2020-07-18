package cn.eric.h2.service.impl;

import cn.eric.h2.notify.NotifyEvent;
import cn.eric.h2.notify.NotifyEventFactory;
import cn.eric.h2.notify.NotifyEventHandler;
import cn.eric.h2.notify.NotifyEventHandlerException;
import cn.eric.h2.service.DisruptorService;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.Executors;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: DisruptorServiceImpl
 * @Description: TODO
 * @company lsj
 * @date 2019/2/18 11:23
 **/
@Service
public class DisruptorServiceImpl implements DisruptorService, DisposableBean, InitializingBean {

    private Disruptor<NotifyEvent> disruptor;

    private static final int RING_BUFFER_SIZE = 1024 * 1024;

    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        disruptor = new Disruptor<NotifyEvent>(new NotifyEventFactory(),RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new NotifyEventHandlerException());
        disruptor.handleEventsWith(new NotifyEventHandler());
        disruptor.start();
    }

    @Override
    public void sendNotify(String message) {
        RingBuffer<NotifyEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence, data) -> event.setMessage(data), message);
    }

    /**
     * @MethodName: readLineNumberToQueue
     * @Description: 读取文件 从第几行 到第几行
     * @Param: [file, beginLine, endLines]
     * @Return: void
     * @Author: YCKJ2725
     * @Date: 2020/6/4 18:27
     **/
    @Override
    public void readLineNumberToQueue(File file, long beginLine, long endLine) throws IOException {
        if (beginLine < 0 || beginLine > endLine) {
            System.out.println("传入行数" + beginLine + "有误，不在范围之内");
        } else {
            InputStreamReader inputReader = null;
            BufferedReader bufferReader = null;
            try {
                InputStream inputStream = new FileInputStream(file);
                inputReader = new InputStreamReader(inputStream, "UTF-8");
                bufferReader = new BufferedReader(inputReader);

                // 读取
                String lineContent = null;
                int currentReadLine = 0;
                while ((lineContent = bufferReader.readLine()) != null) {
                    if(currentReadLine < beginLine){
                        currentReadLine++;
                        continue;
                    }
                    currentReadLine++;
                    // lineContent 放入队列中
                    sendNotify(lineContent);
                    if (currentReadLine >= endLine) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bufferReader != null) {
                    bufferReader.close();
                }
                if(inputReader != null) {
                    inputReader.close();
                }
            }
        }
    }
}
