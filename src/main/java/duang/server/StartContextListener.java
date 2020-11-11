package duang.server;

import cn.hutool.core.util.ClassUtil;
import duang.exception.DuangException;
import duang.ioc.IocFactory;
import duang.mvc.common.beans.BeanFactory;
import duang.mvc.plugin.PluginFactory;
import duang.mvc.route.RouteFactory;
import duang.utils.ScanFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 启动监听器
 */
public class StartContextListener {

    private static class SingletonHolder {
        private static final StartContextListener INSTANCE = new StartContextListener();
    }

    public static StartContextListener duang() {
        return SingletonHolder.INSTANCE;
    }

    /**
     *  netty启动后，需要对框架执行以下操作，顺序不可变更
     *  1，扫描类
     *  2，类实例化
     *  3，加载插件
     *  4，依赖注入
     *  5，注册路由
     *  6，执行自定义的初始化代码
     */
    private List<Class<?>> APP_CONTEXT_LISTENER = new ArrayList<Class<?>>(){
        {
            this.add(ScanFactory.class);
            this.add(BeanFactory.class);
            this.add(PluginFactory.class);
            this.add(IocFactory.class);
            this.add(RouteFactory.class);
        }
    };

    /**
     * 启动框架
     */
    public void start() {
        try {
            for(Iterator<Class<?>> it = APP_CONTEXT_LISTENER.iterator(); it.hasNext();) {
                ClassUtil.loadClass(it.next().getName(), true);
            }
        } catch (Exception e) {
            throw new DuangException(e.getMessage(), e);
        }
    }
}
