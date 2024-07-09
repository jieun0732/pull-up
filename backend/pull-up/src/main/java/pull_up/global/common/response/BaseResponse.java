package pull_up.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 서버 요청에 대한 기본 응답 정의.
 */
@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    private int status;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(int status, String message, T data) {
        return new BaseResponse<>(status, message, data);
    }

    public static BaseResponse<Void> error(int status, String message) {
        return new BaseResponse<>(status, message, null);
    }

}
