package duang.spi;

/**
 *  Advice接口，
 *  判断是否允许通过，例如文件是否允许上传之类的
 *
 * @param <T>
 */
public interface IAdvice<T> {

    boolean handler(T adviceObj);

}
