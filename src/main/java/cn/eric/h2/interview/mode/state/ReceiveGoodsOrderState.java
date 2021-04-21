package cn.eric.h2.interview.mode.state;

/**
 * @ClassName ReceiveGoodsOrderState
 * @Description: 已收货状态类
 * @Author YCKJ2725
 * @Date 2021/4/21
 * @Version V1.0
 **/
public class ReceiveGoodsOrderState extends AbstractOrderState {
    public ReceiveGoodsOrderState(OrderContext orderContext) {
        super(orderContext);
    }

    @Override
    public void payOrder() {
        System.out.println("您已经付过钱啦，不要重复付钱哦");
    }

    @Override
    public void deliver() {
        System.out.println("商品已发货并送达，请不要重复发货");
    }

    @Override
    public void receiveGoods() {
        System.out.println("用户已收到商品，此次交易结束");
    }
}
