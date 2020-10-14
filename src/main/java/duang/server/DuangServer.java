package duang.server;

import duang.mvc.common.enums.ServerType;
import duang.mvc.plugin.PluginFactory;
import duang.mvc.route.RouteFactory;
import duang.utils.ScanFactory;

public class DuangServer {

    private String host = "0.0.0.0";
    private Integer port = 5050;
    private IWebServer server;

    private static class SingletonHolder {
        private static final DuangServer INSTANCE = new DuangServer();
    }
    private DuangServer() {
        // 扫描类
        ScanFactory.scan();
        // 初始化插件
        PluginFactory.initPlugin();
        // 初始化路由
        RouteFactory.initRoute();
    }
    public static DuangServer duang() {
        return SingletonHolder.INSTANCE;
    }

    public DuangServer host(String host) {
        this.host = host;
        return this;
    }
    public DuangServer port(Integer port) {
        this.port = port;
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
