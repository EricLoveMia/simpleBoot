package cn.eric.h2.interview.mode.singleton.register;

/**
 * @ClassName EnumSingletonTest
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/16
 * @Version V1.0
 **/
public class EnumSingletonTest {
    public static void main(String[] args) {
        System.out.println(EnumSingle.getInstance());
        System.out.println(EnumSingle.getInstance());
        System.out.println(EnumSingle.getInstance() == EnumSingle.getInstance());
    }
}
