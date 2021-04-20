package cn.eric.h2.interview.mode.command;

/**
 * @ClassName CdCommand
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/20
 * @Version V1.0
 **/
public class CdCommand implements ICommand {
    private LinuxSystem linuxSystem;

    public CdCommand(LinuxSystem linuxSystem) {
        this.linuxSystem = linuxSystem;
    }

    @Override
    public void execute() {
        linuxSystem.cd();
    }
}
