package duang.mvc.common.dto;

/**
 * 参数或实体类验证失败对象
 *
 * @author Laotang
 * @since
 */
public class ValidDto implements java.io.Serializable {

    private String fieldName;
    private String errorMsg;

    public ValidDto() {
    }

    public ValidDto(String fieldName, String errorMsg) {
        this.fieldName = fieldName;
        this.errorMsg = errorMsg;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
