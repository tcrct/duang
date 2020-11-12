package duang.spi;

import cn.hutool.core.io.FileUtil;
import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件上传意见处理
 */
public abstract class DefaultUploadFileAdvice implements IAdvice<UploadFileDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUploadFileAdvice.class);
    private static Long MAX_FILE_SIZE = Long.MAX_VALUE;
    private static Set<String> EXT_NAME_LIST = new HashSet<String>(){{
        this.add("jpg");
        this.add("jpeg");
        this.add("bmp");
        this.add("png");
        this.add("gif");
        this.add("rar");
    }};

    @Override
    public void handler(File fileItem, UploadFileDto uploadFileDto) throws Exception {
        if (uploadFileDto.getFileSize() > MAX_FILE_SIZE) {
            throw new DuangException(String.format("文件大小超出限制，只允许上传小于[%s]的文件", MAX_FILE_SIZE));
        }
        if (!EXT_NAME_LIST.contains(uploadFileDto.getExtName().toLowerCase())) {
            throw new DuangException(String.format("该文件类型[%s]不允许上传", uploadFileDto.getExtName()));
        }
        String target =  SettingKit.duang().get("upload.file.storage");
        if (ToolsKit.isEmpty(target) || "local".equalsIgnoreCase(target)) {
            local(fileItem, uploadFileDto);
        }  else {
            other(fileItem, uploadFileDto);
        }
    }
    public void local(File fileItem, UploadFileDto uploadFileDto) {
        // 创建目录
        FileUtil.mkParentDirs(uploadFileDto.getServerFilePath());
        // 保存文件
        FileUtil.copy(fileItem, uploadFileDto.getFile(), true);
    }

    public abstract void other(File fileItem, UploadFileDto uploadFileDto);

}
