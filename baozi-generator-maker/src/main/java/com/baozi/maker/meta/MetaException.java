package com.baozi.maker.meta;

/**
 * @author zwb
 * @date 2024/12/22 20:59
 * @since 2024.0.1
 **/
public class MetaException extends RuntimeException{

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
