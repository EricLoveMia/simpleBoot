package cn.eric.h2.interview.mode.observer;

/**
 * @ClassName WeatherDataTest
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class WeatherDataTest {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();//气象数据即被观察者
        WeatherDisplay weatherDisplay = new WeatherDisplay(weatherData);//天气展示即观察者
        weatherData.setMessurements(37.2f, 80f, 32.5f);
        weatherDisplay.display();//天气展示
        weatherData.setMessurements(36.2f, 70f, 33.5f);
        weatherDisplay.display();//天气展示
    }
}
