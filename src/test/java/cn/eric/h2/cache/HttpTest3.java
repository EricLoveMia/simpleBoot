package cn.eric.h2.cache;

import cn.eric.h2.util.http.HttpClientUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @ClassName HttpTest
 * @Description: http性能测试
 * @Author YCKJ2725
 * @Date 2020/5/13
 * @Version V1.0
 **/
public class HttpTest3 {

    static final String url = "https://blog.csdn.net/money9sun/article/details/99458302";

    static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    static ExecutorService executorService = new ThreadPoolExecutor(20, 20, 0L
            , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int N = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            executorService.execute(() -> {

                // 每次都新建http请求
                try {
                    HttpClientUtil.sendGet(url);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("end" + (System.currentTimeMillis() - start));

        /**  工具类
         *  HttpClientUtil 72秒
         *  HttpUtils  94秒
         *  HttpClientUtils  很久 没有运行完 每次都new一个client
         * */

        executorService.shutdownNow();
    }
}
