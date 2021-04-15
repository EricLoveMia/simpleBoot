package cn.eric.h2.interview.mode.responsibility;

import lombok.Data;

/**
 * @ClassName MyHandler
 * @Description: 创建一个Handler抽象类, 这个类维护了链路中的下一个对象，并且将真正处理逻辑的方法doHandler只进行了抽象定义，具体留给实现类去实现
 * @Author YCKJ2725
 * @Date 2021/4/13
 * @Version V1.0
 **/
@Data
public abstract class MyHandler {
    protected MyHandler nextHandler;

    public abstract void doHandler(LoginUser loginUser);
}
