package duang.mvc.common.core;

import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;

import java.util.List;

public abstract class BaseController {

    private IRequest request;
    private IResponse response;
//    private Render render;

    protected IRequest getRequest() {
        return request;
    }

    protected IResponse getResponse() {
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

    protected List<UploadFileDto> getUploadFiles() throws Exception{
        String dirPath = "E:\\app";
        return getRequest().getUploadFiles(dirPath);
    }

}
