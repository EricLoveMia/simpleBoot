package cn.eric.h2.interview.mode.command;

/**
 * @ClassName LsCommand
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/20
 * @Version V1.0
 **/
public class LsCommand implements ICommand {
    private LinuxSystem linuxSystem;

    public LsCommand(LinuxSystem linuxSystem) {
        this.linuxSystem = linuxSystem;
    }

    @Override
    public void execute() {
        linuxSystem.ls();
    }
}
