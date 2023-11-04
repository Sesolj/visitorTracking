package tracking.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        this.error = e;
    }
}
