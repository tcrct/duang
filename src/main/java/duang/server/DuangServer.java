package duang.server;

import duang.mvc.common.enums.ServerType;

public class DuangServer {

    private String host = "0.0.0.0";
    private Integer port = 8080;
    private IWebServer server;
    private Integer webSocketPort = 7070;

    private static class SingletonHolder {
        private static final DuangServer INSTANCE = new DuangServer();
    }

    private DuangServer() {
        StartContextListener.duang().start();
    }

    public static DuangServer duang() {
        return SingletonHolder.INSTANCE;
    }

    public DuangServer host(String host) {
        this.host = host;
        return this;
    }
    public DuangServer port(Integer httpPort) {
        this.port = httpPort;
        return this;
    }
    public DuangServer webSocket(Integer webSocketPort) {
        this.webSocketPort = webSocketPort;
        return this;
    }
    public DuangServer type(ServerType serverType) {
        server = serverType.getServer();
        return this;
    }

    public void run() {
        if (null == server) {
            server = ServerType.UNDERTOW.getServer();
        }
        server.run(host, port);
    }

}
