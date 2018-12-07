package com.exception;


/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
