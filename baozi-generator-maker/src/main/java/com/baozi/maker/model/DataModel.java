package com.baozi.maker.model;

import lombok.Data;

/**
 * 动态模版配置
 *
 * @author zwb
 * @date 2024/12/1 21:18
 * @since 2024.0.1
 **/
@Data
public class DataModel {

    /**
     * 是否生成循环
     */
    private boolean loop;

    /**
     * 作者注释
     */
    private String author;

    /**
     * 输出信息
     */
    private String outputText;
}
