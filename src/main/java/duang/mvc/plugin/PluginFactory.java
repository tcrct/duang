package duang.mvc.plugin;

import cn.hutool.core.util.ReflectUtil;
import duang.mvc.common.annotation.Plugin;
import duang.utils.ScanFactory;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * 插件工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class PluginFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginFactory.class);

    /**
     * 系统启动前的播件集合
     */
    private static List<IPlugin> PLUGIN_BEFORE_LIST = new ArrayList<>();
    /**
     * 系统启动后的挺像集合
     */
    private static List<IPlugin> PLUGIN_AFTER_LIST = new ArrayList<>();


    public static List<IPlugin> getPluginBeforeList() {
        if (ToolsKit.isEmpty(PLUGIN_BEFORE_LIST)) {
            initPlugins();
        }
        return PLUGIN_BEFORE_LIST;
    }

    public static List<IPlugin> getPluginAfterList() {
        if (ToolsKit.isEmpty(PLUGIN_AFTER_LIST)) {
            initPlugins();
        }
        return PLUGIN_AFTER_LIST;
    }

    private static void initPlugins() {
        Set<Class<?>> pluginSet = ScanFactory.getClassListByAnnotation(Plugin.class);
        if (ToolsKit.isEmpty(pluginSet)) {
            LOGGER.warn("没有发现启动插件类");
            return;
        }
        try {
            TreeMap<Integer, IPlugin> beforePluginMap = new TreeMap<>();
            TreeMap<Integer, IPlugin> afterPluginMap = new TreeMap<>();
            for (Class<?> clazz : pluginSet) {
                Class[] interfaces = clazz.getInterfaces();
                if (null != interfaces && interfaces.length==0) {
                    interfaces = clazz.getSuperclass().getInterfaces();
                }
                if (!IPlugin.class.equals(interfaces[0])) {
                    throw new RuntimeException(String.format("[{}]没有实现[%s]接口，请检查！", clazz.getName(), Plugin.class.getSimpleName()));
                }
                Plugin pluginAnn = clazz.getAnnotation(Plugin.class);
                if (null == pluginAnn) {
                    continue;
                }
                IPlugin plugin = (IPlugin) ReflectUtil.newInstance(clazz);
                if (ToolsKit.isNotEmpty(plugin)) {
                    if (pluginAnn.after()) {
                        afterPluginMap.put(pluginAnn.sort(), plugin);
                    } else {
                        beforePluginMap.put(pluginAnn.sort(), plugin);
                    }
                }
            }

            if(!beforePluginMap.isEmpty()) {
                PLUGIN_BEFORE_LIST.addAll(beforePluginMap.values());
            }
            if(!afterPluginMap.isEmpty()) {
                PLUGIN_AFTER_LIST.addAll(beforePluginMap.values());
            }
        } catch (Exception e) {
            LOGGER.warn("初始化插件时出错时出错: {}，清空PLUGIN_LIST集合后退出", e.getMessage(), e);
            PLUGIN_BEFORE_LIST.clear();
            PLUGIN_AFTER_LIST.clear();
        }
    }


}
