package com.baozi.cli.command;

import picocli.CommandLine;

/**
 * @author zwb
 * @date 2024/12/11 23:39
 * @since 2024.0.1
 **/
@CommandLine.Command(name = "CommandExecutor", version = "CommandExecutor 1.0", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{

    private final CommandLine commandLine;

    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }

    @Override
    public void run() {
        // 不输入子命令，给出友好提示
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }
}
