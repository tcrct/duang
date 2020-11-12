package duang.server.abstracts;

import cn.hutool.core.util.ReflectUtil;
import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.mvc.http.IRequest;
import duang.spi.IAdvice;
import duang.spi.DefaultUploadFileAdvice;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRequest implements IRequest {

    protected String requestId;
    protected Map<String,String> headerMap = new ConcurrentHashMap<>();
    protected Map<String,String> paramMap = new ConcurrentHashMap<>();

    protected Charset ISO_8859_1 = Charset.forName("ISO_8859_1");
    private static IAdvice uploadFileAdvice;

    protected boolean isAllowUpload(UploadFileDto uploadFileDto) throws DuangException {
        if (null == uploadFileAdvice) {
            String uploadFileAdviceClassStr = SettingKit.duang().get("upload.file.advice");
            uploadFileAdvice = ToolsKit.isNotEmpty(uploadFileAdviceClassStr) ? ReflectUtil.newInstance(uploadFileAdviceClassStr) : new DefaultUploadFileAdvice();
        }
        return uploadFileAdvice.handler(uploadFileDto);
    }
}
