package cn.eric.h2.notify;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: NotifyEventHandler
 * @Description: TODO
 * @company lsj
 * @date 2019/2/18 11:06
 **/
public class NotifyEventHandler implements EventHandler<NotifyEvent>, WorkHandler<NotifyEvent> {

    Logger logger = LoggerFactory.getLogger(NotifyEventHandler.class);

    @Override
    public void onEvent(NotifyEvent notifyEvent, long l, boolean b) throws Exception {
        System.out.println("接收到消息");
        this.onEvent(notifyEvent);
    }

    @Override
    public void onEvent(NotifyEvent notifyEvent) throws Exception {
        System.out.println(notifyEvent+">>>"+ JSON.toJSONString(notifyEvent));
        // TODO

    }

}
