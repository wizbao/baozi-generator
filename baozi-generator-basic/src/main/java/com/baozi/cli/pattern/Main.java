package com.baozi.cli.pattern;

/**
 * 命令模式
 * 客户端，比如使用遥控器的人
 *
 * @author zwb
 * @date 2024/12/11 22:34
 * @since 2024.0.1
 **/
public class Main {
    public static void main(String[] args) {
        Device device1 = new Device("air conditioner");
        Command turnOff = new TurnOffCommand(device1);
        Command turnOn = new TurnOnCommand(device1);
        RemoteControl remoteControl = new RemoteControl();

        remoteControl.setCommand(turnOff);
        remoteControl.pressButton();

        remoteControl.setCommand(turnOn);
        remoteControl.pressButton();
    }
}
