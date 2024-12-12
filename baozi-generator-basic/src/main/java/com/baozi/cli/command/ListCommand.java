package com.baozi.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

/**
 * @author zwb
 * @date 2024/12/11 23:43
 * @since 2024.0.1
 **/
@CommandLine.Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    @Override
    public void run() {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        String inputPath = new File(parentFile, "samples/acm-template").getAbsolutePath();
        FileUtil.loopFiles(inputPath).forEach(System.out::println);
    }
}
