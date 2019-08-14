package cn.eric.h2.runner;

import cn.eric.h2.cache.CacheEntity;
import cn.eric.h2.queue.QueueOffer;
import cn.eric.h2.util.FileUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: CacheInitRunner
 * @Description: TODO
 * @company lsj
 * @date 2019/5/29 16:57
 **/
@Component
@Order(1)
public class CacheInitRunner implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        System.out.println("load cache..." + Arrays.asList(args));
        new Thread(() -> {
            List<CacheEntity> list = null;
            while (true) {
                // 每10秒 刷新一次文件
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 获取队列的所有数据
                if (QueueOffer.getOfferQueue().size() > 0) {
                    list = new ArrayList<>();
                    CacheEntity entity = null;
                    for (int i = 0, length = QueueOffer.getOfferQueue().size(); i < length; i++) {
                        try {
                            entity = QueueOffer.getOfferQueue().consume();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.add(entity);
                    }

                    // 先分组 然后按照分组写入文件中
                    Map<String, List<CacheEntity>> collect = list.stream().collect(Collectors.groupingBy(CacheEntity::getKey));
                    Set<Map.Entry<String, List<CacheEntity>>> entries = collect.entrySet();
                    Iterator<Map.Entry<String, List<CacheEntity>>> iterator = entries.iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, List<CacheEntity>> next = iterator.next();
                        List<CacheEntity> value = next.getValue();
                        Map<String, CacheEntity> collect1 = value.stream().collect(Collectors.toMap(CacheEntity::getKey, a -> a));
                        FileUtil.addToFile(collect1, next.getKey().substring(0, 1).toLowerCase());
                    }
                } else {
                    System.out.println("暂无缓存数据写入文件");
                }
            }
        }).start();
    }
}
