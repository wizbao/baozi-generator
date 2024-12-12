package com.baozi.cli.pattern;

/**
 * 接收者，比如设备
 *
 * @author zwb
 * @date 2024/12/11 22:24
 * @since 2024.0.1
 **/
public class Device {

    private final String name;

    public Device(String name) {
        this.name = name;
    }

    public void turnOff() {
        System.out.println("turn off " + name);
    }

    public void turnOn() {
        System.out.println("turn on " + name);
    }
}
