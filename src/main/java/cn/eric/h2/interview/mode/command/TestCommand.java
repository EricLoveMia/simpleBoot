package cn.eric.h2.interview.mode.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestCommand
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/20
 * @Version V1.0
 **/
public class TestCommand {

    public static void main(String[] args) {
        LinuxSystem linuxSystem = new LinuxSystem();
        List<ICommand> commandList = new ArrayList<>();
        commandList.add(new CdCommand(linuxSystem));
        commandList.add(new LsCommand(linuxSystem));
        commandList.add(new RestartCommand(linuxSystem));

        XshellInvoker xshellInvoker = new XshellInvoker(commandList);

        xshellInvoker.execute(new LsCommand(linuxSystem));//执行指定命令
        System.out.println("------------------------");
        xshellInvoker.executeCdAndLs();//指定特定命令宏
        System.out.println("------------------------");
        xshellInvoker.executeAll();//执行全部命令
    }

}
