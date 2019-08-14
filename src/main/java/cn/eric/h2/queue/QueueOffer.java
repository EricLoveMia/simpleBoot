package cn.eric.h2.queue;

import cn.eric.h2.cache.CacheEntity;

import javax.management.Query;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: QueueFactory
 * @Description: 队列工厂 单例模式
 * @company lsj
 * @date 2019/8/13 15:59
 **/
public class QueueOffer {

    static final int QUEUE_MAX_SIZE = 1024;

    static BlockingQueue<CacheEntity> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private QueueOffer(){}

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static QueueOffer factory = new QueueOffer();
    }

    public static QueueOffer getOfferQueue(){
        return SingletonHolder.factory;
    }

    /**
     * 入队
     * @param obj
     * @throws InterruptedException
     * add(e) 队列未满时，返回true；队列满则抛出IllegalStateException(“Queue full”)异常——AbstractQueue
     * put(e) 队列未满时，直接插入没有返回值；队列满时会阻塞等待，一直等到队列未满时再插入。
     * offer(e) 队列未满时，返回true；队列满时返回false。非阻塞立即返回。
     * offer(e, time, unit) 设定等待的时间，如果在指定时间内还不能往队列中插入数据则返回false，插入成功返回true。
     */
    public Boolean produce(CacheEntity obj) throws InterruptedException {
        return blockingQueue.offer(obj);
    }

    /**
     * 消费出队
     * poll() 获取并移除队首元素，在指定的时间内去轮询队列看有没有首元素有则返回，否者超时后返回null
     * take() 与带超时时间的poll类似不同在于take时候如果当前队列空了它会一直等待其他线程调用notEmpty.signal()才会被唤醒
     */
    public CacheEntity consume() throws InterruptedException {
        return blockingQueue.poll();
    }

    // 获取队列大小
    public int size() {
        return blockingQueue.size();
    }




}
