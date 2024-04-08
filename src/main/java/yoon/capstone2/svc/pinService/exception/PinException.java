package yoon.capstone2.svc.pinService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PinException extends RuntimeException{

    private final String message;
    private final HttpStatus status;

    public PinException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
