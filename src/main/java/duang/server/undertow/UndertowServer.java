package duang.server.undertow;

import duang.mvc.handler.HandlerFactory;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.mvc.http.RequestResponseFactory;
import duang.server.IWebServer;
import duang.utils.ToolsKit;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.util.HttpString;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.util.Iterator;
import java.util.Map;

import static io.undertow.Handlers.*;

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
                //BlockingHandler 要包多一层，若不然，post json取不到
                .setHandler(path(new BlockingHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        IRequest request = new UndertowRequest(exchange);
                        IResponse response = new UndertowResponse(request, exchange);
//                        RequestResponseFactory.create(request, response);
                        HandlerFactory.handler(request, response);
                        for (Iterator<Map.Entry<String,String>> iterator = response.header().entrySet().iterator(); iterator.hasNext();) {
                            Map.Entry<String,String> entry = iterator.next();
                            exchange.getResponseHeaders().put(HttpString.tryFromString(entry.getKey()), entry.getValue());
                        }
                        exchange.getResponseSender().send(response.body());
                    }
                }))
                .addPrefixPath(ToolsKit.getWsMappingPrefixPath(), websocket(new UndertowWebSocket())))
                .build();
        server.start();
    }

    private void start1() {
//        Undertow server = Undertow.builder()
//                .addHttpListener(getPort(), getHost())
//                .setWorkerThreads(undertowConf.getWorkerThreads())
//                .setIoThreads(undertowConf.getIoThreads())
//                .setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, undertowConf.isAllowUnescapedCharactersInUrl())
//                .setServerOption(UndertowOptions.ENABLE_HTTP2, undertowConf.isHttp2enabled())
//               //  BlockingHandler 要包多一层，若不然，post json取不到
//                .setHandler(new BlockingHandler(new EncodingHandler(new HttpHandler() {
//                    @Override
//                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
//                        IRequest request = new UndertowRequest(exchange);
//                        IResponse response = new UndertowResponse(request, exchange);
////                        RequestResponseFactory.create(request, response);
//                        HandlerFactory.handler(request, response);
//                        for (Iterator<Map.Entry<String,String>> iterator = response.header().entrySet().iterator(); iterator.hasNext();) {
//                            Map.Entry<String,String> entry = iterator.next();
//                            exchange.getResponseHeaders().put(HttpString.tryFromString(entry.getKey()), entry.getValue());
//                        }
//                        exchange.getResponseSender().send(response.body());
//                    }
//                }, new ContentEncodingRepository()
//                        .addEncodingHandler("")
//                        .addEncodingHandler("gzip", new GzipEncodingProvider(undertowConf.getGzipLevel()),
//                                undertowConf.getGzipPriority(), this::gzipEnabled)))).build();
//        server.start();
    }
}
