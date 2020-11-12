package duang.spi;

import duang.exception.DuangException;
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
        if (resultObj instanceof DuangException) {
            DuangException duangException = (DuangException)resultObj;
            resultObj = duangException.getExceptionObj();
            headDto.setCode(1);
            headDto.setMsg("参数验证失败");
        }
        headDto.setProcessTime(System.currentTimeMillis() - headDto.getStartTime());
        ReturnDto<String> returnDto = new ReturnDto(headDto, resultObj);

        ToolsKit.removeThreadLocalDto();
        return ToolsKit.toJsonString(returnDto);
    }
}
