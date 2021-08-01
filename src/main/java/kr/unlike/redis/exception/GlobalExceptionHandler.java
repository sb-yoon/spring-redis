package kr.unlike.redis.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.unlike.redis.dto.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handler(MissingServletRequestParameterException e, HttpServletRequest request) {
    	Result result = new Result(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handler(HttpMessageNotReadableException e, HttpServletRequest request) {
    	Result result = new Result(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handler(BizException e, HttpServletRequest request) {
    	Result result = new Result(HttpStatus.NOT_FOUND.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handler(InvalidTokenException e, HttpServletRequest request) {
    	Result result = new Result(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handler(Exception e, HttpServletRequest request) {
    	Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}