package yoon.capstone2.svc.pinService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RuntimeException{
    private final String message;

    private final HttpStatus status;


    public UnauthorizedException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
