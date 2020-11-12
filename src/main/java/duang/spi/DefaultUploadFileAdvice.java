package duang.spi;

import duang.Duang;
import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * 文件上传意见处理
 */
public class DefaultUploadFileAdvice implements  IAdvice<UploadFileDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUploadFileAdvice.class);
    private static Long MAX_FILE_SIZE = 10086L;
    private static Set<String> EXT_NAME_LIST = new HashSet<String>(){{
        this.add("jpg");
        this.add("jpeg");
        this.add("bmp");
        this.add("png");
        this.add("gif");
    }};

    @Override
    public boolean handler(UploadFileDto uploadFileDto) throws DuangException {
        LOGGER.info(ToolsKit.toJsonString(uploadFileDto));
        if (uploadFileDto.getFileSize() > MAX_FILE_SIZE) {
            throw new DuangException(String.format("文件大小超出限制，只允许上传小于[%s]的文件", MAX_FILE_SIZE));
        }
        if (!EXT_NAME_LIST.contains(uploadFileDto.getExtName().toLowerCase())) {
            throw new DuangException(String.format("该文件[%s]不允许上传", uploadFileDto.getExtName()));
        }
        return true;
    }
}
