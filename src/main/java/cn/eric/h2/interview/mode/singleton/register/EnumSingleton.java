package cn.eric.h2.interview.mode.singleton.register;

/**
 * @ClassName EnumSingleton
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public enum EnumSingleton {
    /**
     * 创建一个枚举对象，该对象天生为单例
     **/
    INSTANCE;

    /**
     * 私有化枚举的构造函数
     **/
    EnumSingleton() {

    }

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}
