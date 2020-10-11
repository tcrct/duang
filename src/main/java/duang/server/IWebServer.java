package duang.server;

public interface IWebServer {

    /**
     * 启动
     *
     */
    void run();

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
