package cn.eric.h2.service;

import java.io.File;
import java.io.IOException;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: DisruptorService
 * @Description: TODO
 * @company lsj
 * @date 2019/2/18 11:21
 **/
public interface DisruptorService {

    /**
     * 发送消息
     * @author Eric
     * @date 11:23 2019/2/18
     * @params messge
     * @throws
     * @return void
     **/
    void sendNotify(String messge);

    /**
     * @MethodName: readLineNumberToQueue
     * @Description: 读取文件 并发送消息
     * @Param: [file, beginLine, endLine]
     * @Return: void
     * @Author: YCKJ2725
     * @Date: 2020/6/4 20:07
    **/
    void readLineNumberToQueue(File file, long beginLine, long endLine) throws IOException;
}
