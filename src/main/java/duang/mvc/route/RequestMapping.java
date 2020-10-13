package duang.mvc.route;

import duang.mvc.common.enums.HttpMethod;
import duang.mvc.common.enums.MappingType;

public class RequestMapping  implements java.io.Serializable {

    private String value;
    private String desc;
    private HttpMethod httpMethod;
    private MappingType mappingType;

    public RequestMapping() {
    }

    public RequestMapping(String value, String desc, HttpMethod httpMethod, MappingType mappingType) {
        this.value = value;
        this.desc = desc;
        this.httpMethod = httpMethod;
        this.mappingType = mappingType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }
}
