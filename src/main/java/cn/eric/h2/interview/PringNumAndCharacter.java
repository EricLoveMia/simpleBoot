package cn.eric.h2.interview;

/**
 * @ClassName PringNumAndCharacter
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/8
 * @Version V1.0
 **/
public class PringNumAndCharacter {

    static class PrintRunnable implements Runnable {
        //定义一个锁
        private Object lock;

        public PrintRunnable(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 26; i++) {
                    if (Thread.currentThread().getName().equals("打印数字")) {
                        //打印数字1-26
                        System.out.print((i + 1));
                        // 唤醒其他在等待的线程
                        lock.notifyAll();
                        try {
                            // 让当前线程释放锁资源，进入wait状态
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (Thread.currentThread().getName().equals("打印字母")) {
                        // 打印字母A-Z
                        System.out.print((char) ('A' + i));
                        // 唤醒其他在等待的线程
                        lock.notifyAll();
                        try {
                            if (i == 25) {
                                return;
                            }
                            // 让当前线程释放锁资源，进入wait状态
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 测试main方法
     */
    public static void main(String[] args) {
        Object lock = new Object();
        new Thread(new PrintRunnable(lock), "打印数字").start();
        new Thread(new PrintRunnable(lock), "打印字母").start();
    }

}
