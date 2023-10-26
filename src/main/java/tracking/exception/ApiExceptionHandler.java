package tracking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionEntity> handleException(ApiException ex) {
        ApiExceptionEntity customResponseEntity = new ApiExceptionEntity();

        ExceptionEnum errorCode = ex.getError();
        customResponseEntity.setStatus(errorCode.getStatus());
        customResponseEntity.setMessage(errorCode.getMessage());

        return new ResponseEntity<>(customResponseEntity, errorCode.getStatus());
    }
}
