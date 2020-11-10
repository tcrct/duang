package duang.spi;

import duang.mvc.common.dto.HeadDto;
import duang.mvc.common.dto.ReturnDto;
import duang.utils.ToolsKit;

/**
 * 将Controller返回的对象封装成ReturnDto对象返回
 *
 * @author Laotang
 */
public class ResponseBodyAdvice {

    private static class SingletonHolder {
        private static final ResponseBodyAdvice INSTANCE = new ResponseBodyAdvice();
    }

    public static ResponseBodyAdvice duang() {
        return SingletonHolder.INSTANCE;
    }

    public String write(Object resultObj) {
        if (resultObj instanceof ReturnDto) {
            return ToolsKit.toJsonString(resultObj);
        }
        HeadDto headDto = ToolsKit.getThreadLocalDto();
        ReturnDto returnDto = new ReturnDto(headDto, resultObj);
        ToolsKit.removeThreadLocalDto();
        return ToolsKit.toJsonString(returnDto);
    }
}
