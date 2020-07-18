package cn.eric.h2.config;

import cn.eric.h2.service.DisruptorService;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName FileMonitor
 * @Description: 文件监控
 * @Author YCKJ2725
 * @Date 2020/6/4
 * @Version V1.0
 **/
@Component
public class FileMonitor {

   @Autowired
   private DisruptorService disruptorService;
    /**
     * @description
     * @param
     * @return
     * @version 2.0, 2019/1/25 9:44
     * @author <a href="mailto:Tastill@**.cn">Tastill</a>
     */
    @PostConstruct
    public void initFileMonitor() {
        // 监控目录
        String rootDir = "D:\\data\\file\\logs";
        // 轮询间隔 5 秒
        Integer time = 10;
        long interval = TimeUnit.SECONDS.toMillis(time);
        // 创建一个文件观察器用于处理文件的格式,
        //                        FileFilterUtils.suffixFileFilter(".txt")
        FileAlterationObserver _observer = new FileAlterationObserver(
                rootDir,
                FileFilterUtils.and(
                        FileFilterUtils.fileFileFilter()),  //过滤文件格式
                null);
        FileAlterationObserver observer = new FileAlterationObserver(rootDir);

        observer.addListener(new FileListener(disruptorService)); //设置文件变化监听器
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}