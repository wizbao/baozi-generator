package com.baozi.maker;

import com.baozi.maker.cli.command.CommandExecutor;

/**
 * @author zwb
 * @date 2024/12/12 23:27
 * @since 2024.0.1
 **/
public class Main {
    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        // args = new String[]{"list"};
        // args = new String[]{"config"};
        // args = new String[]{"generate","-l","-a","-o"};
        executor.doExecute(args);
    }
}
