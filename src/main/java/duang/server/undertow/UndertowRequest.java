package duang.server.undertow;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import duang.Duang;
import duang.exception.DuangException;
import duang.mvc.common.dto.UploadFileDto;
import duang.mvc.common.enums.HttpMethod;
import duang.server.abstracts.AbstractRequest;
import duang.spi.IAdvice;
import duang.utils.DuangId;
import duang.utils.ToolsKit;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 转换Undertow请求
 *
 * @author Laotang
 */
public class UndertowRequest extends AbstractRequest {

    private static final Log LOGGER = LogFactory.get(UndertowRequest.class);
    private HttpServerExchange exchange;

    public UndertowRequest(HttpServerExchange exchange) {
        this.requestId = createRequestId();
        this.exchange = exchange;
        init();
    }

    private void init() {
        createRequestHeaderMap();
        createRequestParamMap();
    }

    @Override
    public String requestId() {
        return requestId;
    }

    @Override
    public String body() {
        return IoUtil.read(exchange.getInputStream(), Charset.defaultCharset());
    }

    @Override
    public byte[] bodyAsBytes() {
        return IoUtil.readBytes(exchange.getInputStream());
    }

    @Override
    public Long contentLength() {
        return exchange.getRequestContentLength();
    }

    @Override
    public String contentType() {
        return headerMap.get(Header.CONTENT_TYPE.name());
    }

    @Override
    public String contextPath() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return headerMap;
    }

    @Override
    public String headers(String key) {
        return headerMap.get(key);
    }

    @Override
    public String host() {
        return exchange.getHostName();
    }

    @Override
    public String ip() {
        return exchange.getDestinationAddress().getAddress().getHostAddress();
    }

    @Override
    public String params(String key) {
        return paramMap.get(key);
    }

    @Override
    public Map<String, String> params() {
        return paramMap;
    }

    @Override
    public String pathInfo() {
        return exchange.getRequestPath();
    }

    @Override
    public Integer port() {
        return exchange.getDestinationAddress().getPort();
    }

    @Override
    public String protocol() {
        return exchange.getProtocol().toString();
    }

    @Override
    public HttpMethod requestMethod() {
        return HttpMethod.get(exchange.getRequestMethod().toString().toUpperCase());
    }

    @Override
    public String scheme() {
        return exchange.getRequestScheme();
    }

    @Override
    public String servletPath() {
        return null;
    }

    @Override
    public String uri() {
        return exchange.getRequestURI();
    }

    @Override
    public String url() {
       return exchange.getRequestURL();
    }

    @Override
    public UserAgent userAgent() {
        String userAgent =  headerMap.get(Header.USER_AGENT.toString());
        return ToolsKit.isNotEmpty(userAgent) ? UserAgentUtil.parse(userAgent) : null;
    }

    @Override
    public List<UploadFileDto> getUploadFiles(String serverDirectory) throws DuangException, IOException {
        FormDataParser parser = FormParserFactory.builder().build().createParser(exchange);
        // 防止乱码
        parser.setCharacterEncoding(Charset.defaultCharset().toString());
        FormData formData = parser.parseBlocking();
//        Charset characterEncoding = getCharacterEncoding();
        List<UploadFileDto> uploadFileDtoList = new ArrayList<>();
        try {
            for (String name : formData) {
                Deque<FormData.FormValue> formValues = formData.get(name);
                // 判断formValue是不是文件
                if (formValues.getFirst().isFileItem()) {
                    FormData.FormValue formValue = formValues.getFirst();
                    FormData.FileItem fileItem = formValue.getFileItem();
                    String parameterName = "未知";
                    String oiriginalName = formValue.getFileName();
                    String fileName = DuangId.get().toString() + "." + oiriginalName.substring(oiriginalName.lastIndexOf(".") + 1);
                    String contentType = headerMap.get(Headers.CONTENT_TYPE);
                    long fileSize = fileItem.getFileSize();
                    UploadFileDto uploadFileDto = new UploadFileDto(parameterName, serverDirectory, fileName, oiriginalName, name, contentType, fileSize);
                    isAllowUpload(uploadFileDto);
                    // 获取文件名，这种方式获取的是原文件名，带后缀的
                    // 还可以从formValues.getFirst().getFileItem().getFile().getFileName()里获取文件名，不过这个文件名已经被重新命名了，而且还不带后缀
                    // 创建一个输出流，将文件保存到本地
                    FileOutputStream fos = new FileOutputStream(uploadFileDto.getFile());
                    // 保存文件
                    Files.copy(fileItem.getFile(), fos);
                    fos.close();
                    uploadFileDtoList.add(uploadFileDto);
                }
            }
        } catch (DuangException e) {
            throw new DuangException("文件不允许上传，请检查实现了IAdvice接口类的handler方法。异常信息：" + e.getMessage());
        }
        return uploadFileDtoList;
    }

    private void createRequestHeaderMap() {
        HeaderMap map = exchange.getRequestHeaders();
        if (ToolsKit.isEmpty(map)) {
            return;
        }
        for (Iterator<HeaderValues> iterator = map.iterator(); iterator.hasNext();) {
            String key = iterator.next().getHeaderName().toString();
            String value = map.getFirst(key);
            if (ToolsKit.isNotEmpty(key) && ToolsKit.isNotEmpty(value)) {
                headerMap.put(key, value);
            }
        }
    }

    private void createRequestParamMap() {
        Map<String, Deque<String>> pathParamMap = exchange.getPathParameters();
        Map<String, Deque<String>> queryParamMap = exchange.getQueryParameters();
        createRequestParamMap0(pathParamMap);
        createRequestParamMap0(queryParamMap);
        createRequestParamMap1(FormParserFactory.builder().build().createParser(exchange));
    }

    private void createRequestParamMap0(Map<String, Deque<String>> subMap) {
        if (ToolsKit.isEmpty(subMap)) {
            return;
        }
        for (Iterator<Map.Entry<String, Deque<String>>> iterator = subMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Deque<String>> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue().peek();
            if (ToolsKit.isNotEmpty(key) && ToolsKit.isNotEmpty(value)) {
                this.paramMap.put(key, value);
            }
        }
    }

    private void createRequestParamMap1(FormDataParser parser) {
        parser.setCharacterEncoding(Charset.defaultCharset().toString());
        try {
            FormData formData = parser.parseBlocking();
            for(Iterator<String> iterator = formData.iterator(); iterator.hasNext();) {
                String name = iterator.next();
                FormData.FormValue formValue = formData.getFirst(name);
                if (!formValue.isFileItem()) {
                    paramMap.put(name, formValue.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("取上传文件参数时出错: " + e.getMessage(), e);
        }
    }

    /**解决文件上传乱码
     *  1，在header头Content-Type里添加 ;charset=utf-8
     *  2，在代码里 new String(fileName.getBytes(ISO_8859_1), Charset.defaultCharset());
     */
    private String getFormDataParamName(String name, Charset charset) {
        if (ISO_8859_1.equals(charset)) {
            return new String(name.getBytes(ISO_8859_1), Charset.defaultCharset());
        }
        return name;
    }

//    private Charset getCharacterEncoding() {
//        String contentType = headerMap.get(Headers.CONTENT_TYPE.toString());
//        return contentType == null ? ISO_8859_1 : Charset.forName(Headers.extractQuotedValueFromHeader(contentType, "charset"));
//    }
}
