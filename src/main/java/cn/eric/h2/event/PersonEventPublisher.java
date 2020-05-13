package cn.eric.h2.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName PersonEventPublisher
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/3/25
 * @Version V1.0
 **/
@Component
public class PersonEventPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    //使用ApplicationContext的publishEvent方法来发布
    public void publish(String msg){
        applicationContext.publishEvent(new PersonEvent(this,msg));
    }
}
