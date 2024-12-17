package ${basePackage}.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;

/**
 * @author zwb
 * @date 2024/12/11 23:43
 * @since 2024.0.1
 **/
@CommandLine.Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    @Override
    public void run() {
        FileUtil.loopFiles("${fileConfig.inputRootPath}").forEach(System.out::println);
    }
}
