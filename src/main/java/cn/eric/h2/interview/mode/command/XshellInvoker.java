package cn.eric.h2.interview.mode.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XshellInvoker
 * @Description: 一个用来接收命令的类
 * @Author YCKJ2725
 * @Date 2021/4/20
 * @Version V1.0
 **/
public class XshellInvoker {

    private List<ICommand> commandList = new ArrayList<>();

    public XshellInvoker(List<ICommand> commandList) {
        this.commandList = commandList;
    }

    /**
     * 执行指定命令
     *
     * @param command
     */
    public void execute(ICommand command) {
        command.execute();
    }

    /**
     * 执行命令宏
     */
    public void executeCdAndLs() {
        for (ICommand command : commandList) {
            if (command instanceof LsCommand || command instanceof CdCommand) {
                command.execute();
            }
        }
    }

    public void executeAll() {
        for (ICommand command : commandList) {
            command.execute();
        }
    }
}
