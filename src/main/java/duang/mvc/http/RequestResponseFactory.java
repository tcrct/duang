package duang.mvc.http;

public class RequestResponseFactory {

    private IRequest request;
    private IResponse response;

    public static void create(IRequest request, IResponse response) {
        new RequestResponseFactory(request, response);
    }

    private RequestResponseFactory(IRequest request, IResponse response) {
        this.request = request;
        this.response = response;
    }

    public IRequest getRequest() {
        return request;
    }

    public IResponse getResponse() {
        return response;
    }
}
