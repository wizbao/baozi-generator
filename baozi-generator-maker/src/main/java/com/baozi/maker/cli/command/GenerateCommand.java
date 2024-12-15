package com.baozi.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.baozi.maker.generator.file.FileGenerator;
import com.baozi.maker.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author zwb
 * @date 2024/12/11 23:42
 * @since 2024.0.1
 **/
@Data
@CommandLine.Command(name = "generate", version = "GenerateCommand 1.0", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    /**
     * 是否生成循环
     */
    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否生成循环", interactive = true, arity = "0..1", echo = true)
    private boolean loop;

    /**
     * 作者注释
     */
    @CommandLine.Option(names = {"-a", "--author"}, description = "作者", interactive = true, arity = "0..1", echo = true)
    private String author;

    /**
     * 输出信息
     */
    @CommandLine.Option(names = {"-o", "--outputText"}, description = "输出信息", interactive = true, arity = "0..1", echo = true)
    private String outputText;

    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        FileGenerator.doGenerate(dataModel);
        return 0;
    }
}
