package cn.eric.h2.interview.mode.command;

/**
 * @ClassName LinuxSystem
 * @Description: 真正执行命令类
 * @Author YCKJ2725
 * @Date 2021/4/19
 * @Version V1.0
 **/
public class LinuxSystem {
    public void cd() {
        System.out.println("已经切换到主目录");
    }

    public void ls() {
        System.out.println("已经列举出当前目录下所有文件");
    }

    public void restart() {
        System.out.println("开始重启系统");
    }
}
