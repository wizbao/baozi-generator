package com.baozi.cli.pattern;

/**
 * 具体命令，比如关闭按钮
 *
 * @author zwb
 * @date 2024/12/11 22:26
 * @since 2024.0.1
 **/
public class TurnOffCommand implements Command {

    private final Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }
}
