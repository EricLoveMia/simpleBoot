package cn.eric.h2.interview.mode.observer;

/**
 * @author YCKJ2725
 */
public interface Subject {
    void registerObserver(Observer o);//注册观察对象

    void removeObserver(Observer o);//移除观察对象

    void notifyObservers();//通知观察对象
}
