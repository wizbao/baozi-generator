package com.baozi.maker.generator.main;

/**
 * @author zwb
 * @date 2024/12/15 22:35
 * @since 2024.0.1
 **/
public class MainGenerator extends GenerateTemplate{

    @Override
    protected void buildDist(String outputPath, String shellOutputFilePath, String jarPath, String sourceCopyDestPath) {
        System.out.println("Don't generate dist code");
    }
}

