package duang.mvc.common.enums;

/**
 * duang.setting所设置的key字段枚举
 *
 * @author Laotang
 * @since 1.0
 */
public enum SettingKey implements IEnum {

    MAPPING_PREFIX_PATH("mapping.prefix.path", "","映射前缀，如设置该配置，则会在将该值添加到所有的映射路径的第一级目录位置"),
    WS_MAPPING_PREFIX_PATH("ws.mapping.prefix.path", "","WebSocket映射前缀，如设置该配置，则会在将该值添加到所有的映射路径的第一级目录位置"),
    CHARSET("charset", "UTF-8", "系统统一编码"),
    DATETIME_FORMAT("datetime.format","yyyy-MM-dd HH:mm:ss", "时间格式"),
    SCAN_PACKAGE_PATH("scan.package.path", "", "")

    ;
    private final String key;
    private final String defaultValue;
    private final String desc;
    private SettingKey(String key, String defaultValue, String desc) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.desc = desc;
    }
    public String getKey() {
        return key;
    }
    @Override
    public String getValue() {
        return defaultValue;
    }
    @Override
    public String getDesc() {
        return desc;
    }

}
