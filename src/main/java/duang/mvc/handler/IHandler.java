package duang.mvc.handler;

import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;

/**
 *  处理器类接口
 *
 * @author Laotang
 * @since 1.0
 */
public interface IHandler {

    boolean handler(IRequest request, IResponse response);

}
