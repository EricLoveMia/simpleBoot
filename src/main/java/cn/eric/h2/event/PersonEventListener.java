package cn.eric.h2.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName PersonEventListener
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/3/25
 * @Version V1.0
 **/
@Component
public class PersonEventListener implements ApplicationListener<PersonEvent> {

    @Override
    public void onApplicationEvent(PersonEvent personEvent) {
        String msg = personEvent.getMsg();
        System.out.println("receive message " + msg);
    }
}
