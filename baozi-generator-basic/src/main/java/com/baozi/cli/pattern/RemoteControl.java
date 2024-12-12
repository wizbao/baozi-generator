package com.baozi.cli.pattern;

/**
 * 调用者，比如遥控器
 *
 * @author zwb
 * @date 2024/12/11 22:30
 * @since 2024.0.1
 **/
public class RemoteControl {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}
