package cn.eric.h2.interview.mode.state;

/**
 * @ClassName TestState
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/21
 * @Version V1.0
 **/
public class TestState {
    public static void main(String[] args) {
        OrderContext orderContext = new OrderContext();
        orderContext.payOrder();
        orderContext.payOrder();
        orderContext.deliver();
        orderContext.receiveGoods();
    }
}
