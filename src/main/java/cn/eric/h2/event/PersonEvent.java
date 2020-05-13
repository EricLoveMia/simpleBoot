package cn.eric.h2.event;

import org.springframework.context.ApplicationEvent;

/**
 * @ClassName PersonEvent
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/3/25
 * @Version V1.0
 **/
public class PersonEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private String msg;


    public PersonEvent(Object source,String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
