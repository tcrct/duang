package duang.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 依赖注入工厂类
 *
 * @author Laotang
 */
final public class IocFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(IocFactory.class);

    static {
        initIoc();
        LOGGER.info("initIoc");
    }

    private static void initIoc() {

    }
}
