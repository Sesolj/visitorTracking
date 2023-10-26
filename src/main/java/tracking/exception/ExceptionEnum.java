package tracking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "7일 이상의 데이터는 조회할 수 없습니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "url 정보를 찾을 수 없습니다."),

    //409 CONFLICT 중복된 리소스
    ALREADY_SAVED_URL(HttpStatus.CONFLICT, "이미 저장된 url입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
