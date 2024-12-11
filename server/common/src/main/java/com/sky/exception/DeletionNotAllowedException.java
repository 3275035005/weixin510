package com.sky.exception;

/**
 * 删除异常(不满足删除条件)
 */
public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
