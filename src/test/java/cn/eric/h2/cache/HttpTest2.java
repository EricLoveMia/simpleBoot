package cn.eric.h2.cache;

import cn.eric.h2.util.http.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName HttpTest
 * @Description: http性能测试
 * @Author YCKJ2725
 * @Date 2020/5/13
 * @Version V1.0
 **/
public class HttpTest2 {

    static final String url = "https://aimgr.cloudwalk.cn/mgr/channel/getChannelList?channelType=10021007&status=1";

    static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    static ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L
            , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10240), threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int N = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            Thread.sleep(200);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // HttpClientUtil.sendGet("https://blog.csdn.net/money9sun/article/details/99458302");
                    // HttpUtils.doGet(url,new HashMap<>(),new HashMap<>());

                    // 每次都新建http请求
                    String response = HttpClientUtils.sendGetRequest(url, "UTF-8");
                    JSONObject jsonObject = JSONObject.parseObject(response);
                    Object data = jsonObject.get("data");
                    if(data == null){
                        System.out.println("出现异常");
                    }
                    List list = (List) data;
                    if(list.size() == 0){
                        System.out.println("出现异常");
                    }
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
