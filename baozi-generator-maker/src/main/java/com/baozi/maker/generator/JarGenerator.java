package com.baozi.maker.generator;

import java.io.*;

/**
 * @author zwb
 * @date 2024/12/17 23:20
 * @since 2024.0.1
 **/
public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = otherMavenCommand;

        // 这里一定要拆分‼️
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" ")).directory(new File(projectDir));
        Process process = processBuilder.start();

        // 读取命令的输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        System.out.println("命令执行结束，退出码 = " + process.waitFor());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("/Users/zhangxiao/Home/Projects/code-generator/baozi-generator/baozi-generator-maker/generated/acm-template-pro-generator");
    }

}
