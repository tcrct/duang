package duang.exception;

import cn.hutool.http.HttpStatus;

/**
 * 框架异常
 *
 * @author Laotang
 * @since 1.0
 */
public class DuangException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code = HttpStatus.HTTP_INTERNAL_ERROR;

    public DuangException() {
        super(null, null, false, false);
    }

    public DuangException(int code) {
        this.code = code;
    }

    public DuangException(String exceptionMsg) {
        super(exceptionMsg);
    }

    public DuangException(String exceptionMsg, Exception exception) {
        super(exceptionMsg, exception);
    }

    public DuangException(int code, String exceptionMsg, Exception exception) {
        super(exceptionMsg, exception);
        this.code = code;
    }

     public int code() {
        return code;
    }

    public String getExceptionMsg() {
        return super.getMessage();
    }
}
