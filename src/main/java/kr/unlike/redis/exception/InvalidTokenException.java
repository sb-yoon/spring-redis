package kr.unlike.redis.exception;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 4656084497370658912L;

    public InvalidTokenException(String msg) {
        super(msg);
    }
}
