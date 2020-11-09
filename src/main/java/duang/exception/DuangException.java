package duang.exception;

/**
 * 框架异常
 *
 * @author Laotang
 * @since 1.0
 */
public class DuangException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code = 500;
    private String exceptionMsg;

    public DuangException() {
        super(null, null, false, false);
    }

    public DuangException(int code) {
        this();
        this.code = code;
    }

    public DuangException(String exceptionMsg) {
        this();
        this.exceptionMsg = exceptionMsg;
    }

    public DuangException(int code, String exceptionMsg) {
        this();
        this.code = code;
        this.exceptionMsg = exceptionMsg;
    }

     public int code() {
        return code;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }
}
