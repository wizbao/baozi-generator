package com.baozi.maker.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * @author zwb
 * @date 2024/12/18 00:06
 * @since 2024.0.1
 **/
public class ScriptGenerator {

    public static void doGenerate(String outputPath, String jarPath) {
        // 直接写入脚本文件
        // Linux
        //#!/bin/bash
        // java -jar target/baozi-generator-basic-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"
        StrBuilder sb = new StrBuilder();
        sb.append("#!/bin/bash").append("\n");
        sb.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8), outputPath);
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
        try {
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        } catch (IOException e) {

        }

        // Windows
        sb = new StrBuilder();
        sb.append("@echo off").append("\n");
        sb.append(String.format("java -jar %s %%*", jarPath)).append("\n");
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8), outputPath + ".bat");

    }

}
