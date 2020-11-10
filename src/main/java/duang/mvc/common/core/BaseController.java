package duang.mvc.common.core;

import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;

public abstract class BaseController {

    private IRequest request;
    private IResponse response;
//    private Render render;

    public IRequest getRequest() {
        return request;
    }

    public IResponse getResponse() {
        return response;
    }

    public void init(IRequest request, IResponse response) {
        this.request = request;
        this.response = response;
//        //防止Controller没写returnXXXX方法时，返回上一次请求结果到到客户端
//        this.render = null;
//        if("dev".equalsIgnoreCase(PropKit.get(ConstEnums.PROPERTIES.USE_ENV.getValue()))) {
//            printRequestInfo();
//        }
    }

}
