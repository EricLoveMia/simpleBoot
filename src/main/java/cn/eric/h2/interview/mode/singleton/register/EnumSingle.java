package cn.eric.h2.interview.mode.singleton.register;

/**
 * @ClassName EnumSingleton
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class EnumSingle {
    // 私有化构造函数
    private EnumSingle() {
    }

    // 定义一个静态枚举类
    enum SingletonEnum {
        // 创建一个枚举对象，该对象天生为单例
        INSTANCE;
        private EnumSingle single;

        // 私有化枚举的构造函数
        SingletonEnum() {
            single = new EnumSingle();
        }

        public EnumSingle getInstnce() {
            return single;
        }
    }

    // 对外暴露一个获取 对象的静态方法
    public static EnumSingle getInstance() {
        return SingletonEnum.INSTANCE.getInstnce();
    }
}
