package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //핀 에러
    FORBIDDEN_ACCESS("작성자만 수정/삭제 할 수 있습니다.", HttpStatus.FORBIDDEN),

    PIN_NOT_FOUND("해당 핀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),


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
