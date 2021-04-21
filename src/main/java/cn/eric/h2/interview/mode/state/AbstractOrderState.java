package cn.eric.h2.interview.mode.state;

/**
 * @ClassName AbstractOrderState
 * @Description: 抽象的状态类，这个类需要定义所有状态的所有行为
 * @Author YCKJ2725
 * @Date 2021/4/21
 * @Version V1.0
 **/
public abstract class AbstractOrderState {
    protected OrderContext orderContext;

    public AbstractOrderState(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    public abstract void payOrder();

    public abstract void deliver();

    public abstract void receiveGoods();
}
