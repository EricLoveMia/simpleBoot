package cn.eric.h2.interview.mode.observer;

/**
 * @author YCKJ2725
 */
public interface Observer {
    void update(float temperature, float humidity, float pressure);
}
