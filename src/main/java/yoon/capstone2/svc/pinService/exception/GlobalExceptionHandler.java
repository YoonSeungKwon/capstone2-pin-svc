package yoon.capstone2.svc.pinService.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    Validation Exception
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    public ResponseEntity<String> validationException(MethodArgumentNotValidException e){
//        BindingResult bindingResult  = e.getBindingResult();
//        String exception = bindingResult.getAllErrors().get(0).getDefaultMessage();
//
//
//        return new ResponseEntity<>();
//    }

    @ExceptionHandler({PinException.class})
    public ResponseEntity<String> pinException(PinException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({MapException.class})
    public ResponseEntity<String> mapException(MapException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<String> authException(UnAuthorizedException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

}
