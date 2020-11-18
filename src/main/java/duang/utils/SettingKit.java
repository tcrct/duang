package duang.utils;

import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件工具类
 *
 * @author Laotang
 * @since 1.0
 */
public class SettingKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingKit.class);

    private static SettingKit SETTING_KIT;
    public static final String SETTING_FILE_NANE = "duang.setting";
    private static Setting SETTING = null;

    public static synchronized SettingKit duang() {
        if (null == SETTING_KIT) {
            SETTING_KIT = new SettingKit();
        }
        return SETTING_KIT;
    }

    private SettingKit() {
        SETTING = SettingUtil.get(SETTING_FILE_NANE);
        if (null == SETTING) {
            throw new NullPointerException(String.format("配置文件[%s]设置不正确或不存在，请检查！", SETTING_FILE_NANE));
        }
    }

    public String get(String key) {
        String value = SETTING.get(key);
        if (ToolsKit.isEmpty(value)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("根据[{}]查询配置内容时，内容值为空，请检查！", key);
            }
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        return SETTING.getStr(key, defaultValue);
    }


}
