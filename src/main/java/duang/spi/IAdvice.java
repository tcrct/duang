package duang.spi;

import java.io.File;
import java.io.IOException;

/**
 *  Advice接口，
 *  判断是否允许通过，例如文件是否允许上传之类的
 *
 * @param <T>
 */
public interface IAdvice<T> {

    void handler(File fileItem, T adviceObj) throws Exception;

}
