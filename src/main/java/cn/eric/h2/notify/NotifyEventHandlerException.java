package cn.eric.h2.notify;

import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: NotifyEventHandlerException
 * @Description: TODO
 * @company lsj
 * @date 2019/2/18 11:18
 **/
public class NotifyEventHandlerException implements ExceptionHandler {

    Logger log = LoggerFactory.getLogger(NotifyEventHandlerException.class);
    @Override
    public void handleEventException(Throwable throwable, long sequence, Object event) {
        throwable.fillInStackTrace();
        log.error("process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event.toString(), throwable.getMessage());
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error("start disruptor error ==[{}]!", throwable.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error("shutdown disruptor error ==[{}]!", throwable.getMessage());
    }
}
