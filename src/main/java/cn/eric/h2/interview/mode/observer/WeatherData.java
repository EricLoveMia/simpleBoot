package cn.eric.h2.interview.mode.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WeatherData
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class WeatherData implements Subject {
    private List<Observer> observers;

    private float temperature; //温度
    private float humidity; //湿度
    private float pressure; //气压

    public void setMessurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;

        notifyObservers();//气温信息发生变化时，通知所有观察者
    }

    public WeatherData() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int i = observers.indexOf(observer);
        if (i >= 0) {
            observers.remove(i);
        }
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(e -> {
            e.update(temperature, humidity, pressure);
        });
    }
}
