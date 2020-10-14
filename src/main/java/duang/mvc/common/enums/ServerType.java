package duang.mvc.common.enums;

import duang.server.IWebServer;
import duang.server.netty.NettyServer;
import duang.server.smarthttp.SmartHttpServer;
import duang.server.undertow.UndertowServer;

/**
 * duang.setting所设置的key字段枚举
 *
 * @author Laotang
 * @since 1.0
 */
public enum ServerType {

    UNDERTOW( new UndertowServer(),""),
    NETTY(new NettyServer(), ""),
    SMART_HTTP(new SmartHttpServer(), ""),


    ;
    private final IWebServer server;
    private final String desc;
    private ServerType(IWebServer server, String desc) {
        this.server = server;
        this.desc = desc;
    }
    public IWebServer getServer() {
        return server;
    }
    public String getDesc() {
        return desc;
    }

}
