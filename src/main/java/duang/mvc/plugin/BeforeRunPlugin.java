package duang.mvc.plugin;

/**
 * 框架启动 前 执行抽像类
 *
 * @author Laotang
 * @since 1.0
 */
public abstract class BeforeRunPlugin implements IPlugin {

    /**
     * 在springboot启动 前 执行
     */
    public void before() {
            execute();
    }

    /**
     * 在springboot启动 后 执行
     */
    public void after() {
    }

    protected abstract  void execute();

}
