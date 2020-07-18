package cn.eric.h2.socket.qt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName MqttPushCallback
 * @Description: 传送回调
 * @Author YCKJ2725
 * @Date 2020/6/28
 * @Version V1.0
 **/
public class MqttPushCallback implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MqttPushCallback.class);

    @Override
    public void connectionLost(Throwable cause) {
        log.info("断开连接，建议重连" + this);
        //断开连接，建议重连
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //log.info(token.isComplete() + "");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("Topic: " + topic);
        log.info("Message: " + new String(message.getPayload()));
    }

}