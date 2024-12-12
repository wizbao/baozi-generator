package com.baozi.cli.pattern;

/**
 * 具体命令，比如 打开按钮
 *
 * @author zwb
 * @date 2024/12/11 22:27
 * @since 2024.0.1
 **/
public class TurnOnCommand implements Command {

    private final Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}
