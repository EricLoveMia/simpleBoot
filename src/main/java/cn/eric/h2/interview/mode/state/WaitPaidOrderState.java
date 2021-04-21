package cn.eric.h2.interview.mode.state;

/**
 * @ClassName WaitPaidOrderState
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/21
 * @Version V1.0
 **/
public class WaitPaidOrderState extends AbstractOrderState {

    public WaitPaidOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void payOrder() {
        System.out.println("支付成功");
        // 切换状态
        this.orderContext.setState(this.orderContext.waitDeliver);
    }

    @Override
    public void deliver() {
        System.out.println("对不起，请先付钱");
    }

    @Override
    public void receiveGoods() {
        System.out.println("对不起，请先付钱");
    }
}
