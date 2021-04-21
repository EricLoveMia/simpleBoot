package cn.eric.h2.interview.mode.state;

/**
 * @ClassName OrderContext
 * @Description: 负责状态的切换的
 * @Author YCKJ2725
 * @Date 2021/4/21
 * @Version V1.0
 **/
public class OrderContext {
    AbstractOrderState waitPaid;
    AbstractOrderState waitDeliver;
    AbstractOrderState receiveGoods;

    AbstractOrderState currState;//当前状态

    public OrderContext() {
        this.waitPaid = new WaitPaidOrderState(this);
        this.waitDeliver = new WaitDeliverOrderState(this);
        this.receiveGoods = new ReceiveGoodsOrderState(this);
        currState = waitPaid;
    }

    void setState(AbstractOrderState state) {
        this.currState = state;
    }

    public void payOrder() {
        currState.payOrder();
    }


    public void deliver() {
        currState.deliver();
    }

    public void receiveGoods() {
        currState.receiveGoods();
    }
}
