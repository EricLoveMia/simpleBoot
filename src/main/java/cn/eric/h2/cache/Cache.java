package cn.eric.h2.cache;

import cn.eric.h2.queue.QueueOffer;
import cn.eric.h2.util.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: Cache
 * @Description: TODO
 * @company lsj
 * @date 2019/8/12 18:04
 **/
public class Cache {

    private final static Map<String,Entity> map;

    /** 定时器线程池，用于清除过期的缓存 */
    private static ThreadFactory threadFactory;

    static {
        map = new HashMap<>();
        threadFactory = new ThreadFactoryBuilder().setNameFormat("schedule-pool-%d").build();
        // 载入文件缓存数据
        // 载入所有文件生成index
        File file = new File(FileUtil.basePath);
        File[] tempList = file.listFiles();
        BufferedReader reader = null;
        try {
            for (File log : tempList) {
                if (log.isFile()) {
                    //
                    try {
                        reader = new BufferedReader(new FileReader(FileUtil.basePath + log.getName()));
                        String contentLine;
                        while ((contentLine = reader.readLine()) != null) {
                            String value = contentLine.substring(contentLine.indexOf(":")+1);
                            System.out.println(value);
                            CacheEntity cacheEntity = JSON.parseObject(value, CacheEntity.class);
                            if(cacheEntity.getExpire() == -1) {
                                put(cacheEntity.getKey(), cacheEntity.getValue(),false);
                            }else{
                                if(System.currentTimeMillis() - cacheEntity.getExpire() > 0) {
                                    put(cacheEntity.getKey(), cacheEntity.getValue(),
                                            System.currentTimeMillis() - cacheEntity.getExpire(),false);
                                }else{
                                    // 抛弃
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }



//    ExecutorService threadPool = new ThreadPoolExecutor(1, 1,
//            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), threadFactory,
//            new ThreadPoolExecutor.CallerRunsPolicy());

    //定时器线程池，用于清除过期缓存
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public synchronized static void put(String key,Object data,boolean writeToFile){
        Cache.put(key,data,0,writeToFile);
    }

    public synchronized static void put(String key, Object data, long expire,boolean writeToFile) {
        // 清除原键值对
        // Cache.remove(key);

        if(expire > 0){
            Future future = executor.schedule(new Runnable() {
                @Override
                public void run() {
                    synchronized (key){
                        map.remove(key);
                    }
                }
            },expire,TimeUnit.MILLISECONDS);
            map.put(key,new Entity(data,future));
            // 写入队列，用于刷新到文件中
            if(writeToFile) {
                try {
                    QueueOffer.getOfferQueue().produce(new CacheEntity(key, data, expire));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else{
            map.put(key,new Entity(data,null));
            // 写入队列，用于刷新到文件中
            if(writeToFile) {
                try {
                    QueueOffer.getOfferQueue().produce(new CacheEntity(key, data, -1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public synchronized static Object get(String key) {
        Entity entity = map.get(key);
        return entity == null?null:entity.getValue();
    }

    /**
     * 读取缓存
     *
     * @param key 键
     *            * @param clazz 值类型
     * @return
     */
    public synchronized static <T> T get(String key, Class<T> clazz) {

        return clazz.cast(Cache.get(key));
    }


    /**
     * 清除缓存
     *
     * @param key
     * @return
     */
    public synchronized static Object remove(String key) {
        //清除原缓存数据
        Entity entity = map.remove(key);
        if (entity == null){ return null ;}
        //清除原键值对定时器
        Future future = entity.getFuture();
        if (future != null) { future.cancel(true);}
        return entity.getValue();
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return
     */
    public synchronized static int size() {
        return map.size();
    }

    /**
     * 缓存实体类
     */
    private static class Entity {
        //键值对的value
        private Object value;
        //定时器Future
        private Future future;

        public Entity(Object value, Future future) {
            this.value = value;
            this.future = future;
        }

        /**
         * 获取值
         *
         * @return
         */
        public Object getValue() {
            return value;
        }

        /**
         * 获取Future对象
         *
         * @return
         */
        public Future getFuture() {
            return future;
        }
    }



}
