package duang.mvc.route;

import java.lang.annotation.Annotation;

/**
 * 请求参数对象
 *
 * @author Laotang
 * @since 1.0
 */
public class RequestParam implements java.io.Serializable {

    private Annotation[] annotations;
    private Class<?> paramClass;
    private String name;

    public RequestParam() {
    }

    public RequestParam(Annotation[] annotations, Class<?> paramClass, String name) {
        this.annotations = annotations;
        this.paramClass = paramClass;
        this.name = name;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public Class<?> getParamClass() {
        return paramClass;
    }

    public void setParamClass(Class<?> paramClass) {
        this.paramClass = paramClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
