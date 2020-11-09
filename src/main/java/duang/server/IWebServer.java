package duang.server;

/**
 * 容器接口
 *
 * @author Laotang
 */
public interface IWebServer {

    /**
     * 启动
     *
     */
    void run(String host, Integer port);

    /**
     * 停止
     */
    void stop();

    /**
     * 获取端口
     *
     * @return port
     */
    Integer getPort();

    /**
     * 获取地址
     * @return
     */
    String getHost();

}
