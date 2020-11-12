package duang.server.abstracts;

import cn.hutool.core.util.ReflectUtil;
import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.mvc.http.IRequest;
import duang.spi.IAdvice;
import duang.spi.DefaultUploadFileAdvice;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求的抽象类
 *
 * @author Laotang
 */
public abstract class AbstractRequest implements IRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequest.class);

    protected String requestId;
    protected Map<String,String> headerMap = new ConcurrentHashMap<>();
    protected Map<String,String> paramMap = new ConcurrentHashMap<>();

    protected Charset ISO_8859_1 = Charset.forName("ISO_8859_1");
    private static IAdvice uploadFileAdvice;

    protected void uploadFileToSave(File fileItem, UploadFileDto uploadFileDto) throws Exception {
        if (null == uploadFileAdvice) {
            String uploadFileAdviceClassStr = SettingKit.duang().get("upload.file.advice");
            if (ToolsKit.isEmpty(uploadFileAdviceClassStr)) {
                LOGGER.warn("判断文件是否允许上传时，没有指定实现了IAdvice接口的类，采用默认的DefaultUploadFileAdvice");
                uploadFileAdvice = new DefaultUploadFileAdvice(){
                    @Override
                    public void other(File fileItem, UploadFileDto uploadFileDto) {}
                };
            } else {
                uploadFileAdvice = ReflectUtil.newInstance(uploadFileAdviceClassStr);
            }
        }
        uploadFileAdvice.handler(fileItem, uploadFileDto);
    }
}
