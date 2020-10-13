package duang.mvc.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 框架启动 后 执行的插件类
 *
 * @author Laotang
 * @since 1.0
 */
public abstract class AfterRunPlugin implements IPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfterRunPlugin.class);

    /**
     * 在框架启动 前 执行
     */
    public void before() {}

    /**
     * 在框架启动 后 执行
     */
    public void after() {
        try {
            execute();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    protected abstract  void execute() throws Exception;

}
