package duang.exception;

import cn.hutool.http.HttpStatus;
import duang.mvc.common.dto.HeadDto;
import duang.utils.DuangId;
import duang.utils.DuangThreadLocal;
import duang.utils.ToolsKit;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 框架异常
 *
 * @author Laotang
 * @since 1.0
 */
public class DuangException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static ThreadLocal exceptionThreadLocal= new ThreadLocal();

    private int code = HttpStatus.HTTP_INTERNAL_ERROR;
    private Object exceptionObj = null;

    public DuangException() {
        super(null, null, false, false);
    }

    public DuangException(int code) {
        this.code = code;
    }

    public DuangException(String exceptionMsg) {
        super(exceptionMsg);
    }

    public DuangException(String exceptionMsg, Object exceptionObj) {
        super(exceptionMsg);
        this.exceptionObj = exceptionObj;
    }

    public DuangException(String exceptionMsg, Exception exception) {
        super(exceptionMsg, exception);
    }

    public DuangException(int code, String exceptionMsg, Exception exception) {
        super(exceptionMsg, exception);
        this.code = code;
    }

     public int getCode() {
        return code;
    }

    public String getExceptionMsg() {
        return super.getMessage();
    }

    public Object getExceptionObj() {
        return exceptionObj;
    }
}
