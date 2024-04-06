package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ;


    private final String message;

    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
