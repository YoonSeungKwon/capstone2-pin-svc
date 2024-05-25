package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //핀 에러
    FORBIDDEN_ACCESS("작성자만 수정/삭제 할 수 있습니다.", HttpStatus.FORBIDDEN),

    PIN_NOT_FOUND("해당 핀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    PIN_NOT_VALID("해당 지도의 핀이 아닙니다,", HttpStatus.NOT_FOUND),

    //맵 에러
    MAP_NOT_FOUND("해당 지도를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    //유틸 에러

    NOT_IMAGE_FORMAT("파일의 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    FILE_SIZE_EXCEEDED( "10MB 이하의 파일만 업로드 할 수 있습니다.", HttpStatus.BAD_REQUEST),

    //인증 에러
    UNAUTHORIZED_ACCESS("인증되지 않은 접근입니다.", HttpStatus.UNAUTHORIZED),

    INTERNAL_SERVER_ERROR("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR)
    ;


    private final String message;

    private final HttpStatus status;

    ExceptionCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
