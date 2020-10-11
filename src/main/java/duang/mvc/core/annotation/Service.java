package duang.mvc.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    // 设置Controller是单例还是多例模式, singleton: 单例  prototype: 多例
    String scope() default "prototype";
    // 是否自动注入
    boolean autowired() default true;
}
