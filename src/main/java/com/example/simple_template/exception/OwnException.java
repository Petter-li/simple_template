package com.example.simple_template.exception;

import lombok.Data;

@Data
public class OwnException extends RuntimeException{
    private String msg;
    private int code=500;


    public OwnException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OwnException(String msg, Throwable e) {
        super(msg,e);
        this.msg = msg;
    }

    public OwnException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public OwnException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
