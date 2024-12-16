package ${basePackage};

import ${basePackage}.cli.CommandExecutor;

/**
 * @author zwb
 * @date 2024/12/12 23:27
 * @since 2024.0.1
 **/
public class Main {
    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        executor.doExecute(args);
    }
}
