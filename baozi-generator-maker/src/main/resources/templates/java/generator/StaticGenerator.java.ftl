package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @author zwb
 * @date 2024/12/1 15:05
 * @since 2024.0.1
 **/
public class StaticGenerator {

    public static void copyFilesByHutool(String sourcePath, String targetPath) {
        FileUtil.copy(sourcePath, targetPath, false);
    }
}
