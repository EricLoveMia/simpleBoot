package cn.eric.h2.notify;

import com.lmax.disruptor.EventFactory;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: NotifyEventFactory
 * @Description: TODO
 * @company lsj
 * @date 2019/2/18 11:05
 **/
public class NotifyEventFactory implements EventFactory {
    @Override
    public Object newInstance() {
        return new NotifyEvent();
    }
}
