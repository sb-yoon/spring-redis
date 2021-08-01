package kr.unlike.redis.exception;


public class BizException extends RuntimeException {

    private static final long serialVersionUID = 2447329833156858838L;

    public BizException(String msg) {
        super(msg);
    }
}
