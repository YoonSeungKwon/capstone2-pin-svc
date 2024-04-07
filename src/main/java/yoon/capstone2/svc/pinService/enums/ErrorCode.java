package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    //인증 에러

    UNAUTHORIZED_ACCESS("인증되지 않은 접근입니다.", HttpStatus.UNAUTHORIZED),

    ;


    private final String message;

    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
