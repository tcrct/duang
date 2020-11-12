package duang.mvc.handler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import duang.exception.DuangException;
import duang.mvc.common.dto.HeadDto;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.utils.ToolsKit;
import org.slf4j.MDC;

/**
 * 请求到达业务层，进行一系列初始化处理
 *
 * @author Laotang
 * @since 1.0
 */
public class InitDuangHandler implements IHandler {

    @Override
    public void handler(IRequest request, IResponse response) throws DuangException {
        // 初始化返回对象头部信息
        HeadDto headDto = new HeadDto();
        headDto.setRequestId(request.requestId());
        headDto.setRequestTime(DateUtil.now());
        headDto.setUri(request.uri());
        ToolsKit.setThreadLocalDto(headDto);
        MDC.put(HandlerFactory.LOGBACK_REQ_KEY, request.uri());
    }

}
