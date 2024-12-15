package com.baozi.maker.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.baozi.maker.model.DataModel;
import picocli.CommandLine;

import java.lang.reflect.Field;

/**
 * @author zwb
 * @date 2024/12/11 23:42
 * @since 2024.0.1
 **/
@CommandLine.Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {
    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for (Field field : fields) {
            System.out.println("字段名：" + field.getName());
            System.out.println("字段类型：" + field.getType().getSimpleName());
            System.out.println("------------");
        }
    }
}
