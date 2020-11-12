package duang.spi;

import ch.qos.logback.core.PropertyDefinerBase;
import duang.utils.DuangId;
import duang.utils.ToolsKit;

public class RequestIdDefiner extends PropertyDefinerBase {
    @Override
    public String getPropertyValue() {
        return ToolsKit.getThreadLocalDto().getRequestId();
//        return DuangId.get().toString();
    }
}