package cn.eric.h2.config;

import cn.eric.h2.service.DisruptorService;
import cn.eric.h2.util.FileUtil;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName FileListener
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/6/4
 * @Version V1.0
 **/
public class FileListener extends FileAlterationListenerAdaptor {

    private DisruptorService disruptorService;

    public static final Logger logger = LoggerFactory.getLogger(FileListener.class);

    private static ConcurrentHashMap<String,Long> fileReadMap = new ConcurrentHashMap<>(64);

    /**
     * 读取各文件已经读到的位置
    **/
    static {
        // 有文件

        // 没有文件
    }

    public FileListener(DisruptorService disruptorService) {
        this.disruptorService = disruptorService;
    }
//    @Autowired
//    FileConfigLoader fileConfigLoader;
    /**
     * @description 启动监听
     * @param
     * @return
     * @version 2.0, 2019/1/24 15:08
     * @author <a href="Tastill@**.cn">Tastill</a>
     */
    @Override
    public void onStart(FileAlterationObserver observer) {
        // System.out.println("启动监听器：");
    }
    @Override
    public void onDirectoryCreate(File directory) {
        logger.info("有新文件夹生成："+directory.getName());
    }
    @Override
    public void onDirectoryChange(File directory) {
        logger.info("有文件夹内容发生变化："+directory.getName());
    }
    @Override
    public void onDirectoryDelete(File directory) {
        logger.info("有文件夹被删除："+directory.getName());
    }
    /**
     * @description 文件创建
     * @param
     * @return
     * @version 2.0, 2019/1/24 14:59
     * @author <a href="Tastill@**.cn">Tastill</a>
     */
    @Override
    public void onFileCreate(File file){
        logger.info("有新文件生成："+file.getName());
        //
        try {
            long totalLines = FileUtil.getTotalLines(file);
            fileReadMap.put(file.getAbsolutePath(),totalLines);
            // 开始读取
            disruptorService.readLineNumberToQueue(file,0,totalLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description 文件内容发生变化
     * @param
     * @return
     * @version 2.0, 2019/1/24 15:05
     * @author <a href="Tastill@**.cn">Tastill</a>
     */
    @Override
    public void onFileChange(File file){
        logger.info("有文件被修改："+file.getName());
        // 开始读取
        long totalLines = 0;
        try {
            totalLines = FileUtil.getTotalLines(file);
            disruptorService.readLineNumberToQueue(file,0,totalLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * @description 文件被删除
     * @param
     * @return
     * @version 2.0, 2019/1/24 16:13
     * @author <a href="Tastill@**.cn">Tastill</a>
     */
    @Override
    public void onFileDelete(File file){
        logger.info("有文件被删除："+file.getName());
    }
    /**
     * @description 监听停止
     * @param
     * @return
     * @version 2.0, 2019/1/24 15:07
     * @author <a href="Tastill@**.cn">Tastill</a>
     */
    @Override
    public void onStop(FileAlterationObserver observer){
        // System.out.println("监听停止");
    }
}