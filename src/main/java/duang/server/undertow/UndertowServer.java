package duang.server.undertow;

import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.mvc.http.Request;
import duang.mvc.http.RequestResponseFactory;
import duang.server.IWebServer;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * UndertowServer
 */
public class UndertowServer implements IWebServer {

    private String host;
    private Integer port;

    @Override
    public void run(String host, Integer port) {
        this.host = host;
        this.port = port;
        start();
    }

    @Override
    public void stop() {

    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return host;
    }

    private void start() {
        Undertow server = Undertow.builder()
                .addHttpListener(getPort(), getHost())
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        IRequest request = new UndertowRequest(exchange);
                        IResponse response = new UndertowResponse(request);
                        RequestResponseFactory.create(request, response);
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }
                }).build();
        server.start();
    }
}
