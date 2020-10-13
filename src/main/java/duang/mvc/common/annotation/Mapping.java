package duang.mvc.common.annotation;

import duang.mvc.common.enums.HttpMethod;
import duang.mvc.common.enums.MappingType;

import java.lang.annotation.*;

/**
 * 路由映射注解
 *
 * @author Laotang
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapping {

    /**
     * 映射的路径
     * @return
     */
    String value();

    /**
     * 默认支持所有请求方式
     * @return
     */
    HttpMethod method() default HttpMethod.ALL;

    /**
     * 映射说明
     * @return
     */
    String desc();

    /**
     * 映射类型，分URL与BUTTON
     * @return
     */
    MappingType type() default MappingType.URL;

}
