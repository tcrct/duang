package duang.mvc.http;

final public class Request {

    private static IRequest request;

    public static IRequest duang() {
        return request;
    }

    public Request(IRequest request) {
        this.request = request;
    }

}
