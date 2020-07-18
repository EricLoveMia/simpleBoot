package cn.eric.h2.controller;

import cn.eric.h2.socket.mqtt.MqttGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MqttFunController
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/6/28
 * @Version V1.0
 **/
@RestController
@RequestMapping("/fun")
public class MqttFunController {

    @Autowired
    private MqttGatewayService mqttGateway;

    @RequestMapping("/testMqtt")
    public String sendMqtt(@RequestParam(value = "topic") String topic, @RequestParam(value = "message") String message) {
        mqttGateway.sendToMqtt(message, topic);
        return "SUCCESS";
    }
}
