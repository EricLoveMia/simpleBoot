package cn.eric.h2;

import cn.eric.h2.socket.qt.MqttPushClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class H2Application {

    public static void main(String[] args) {
        SpringApplication.run(H2Application.class, args);
        test();
    }


    private static void test(){
//        MqttPushClient.MQTT_HOST = "tcp://127.0.0.1:1883";
//        MqttPushClient.MQTT_CLIENTID = "client";
//        MqttPushClient.MQTT_USERNAME = "eric";
//        MqttPushClient.MQTT_PASSWORD = "123456";
//        MqttPushClient client = MqttPushClient.getInstance();
//        client.subscribe("eric/topic");
//        client.publish("eric/topic","llllll");
    }
}
