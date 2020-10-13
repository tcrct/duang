package duang.mvc.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Plugin {

    /**排序，数据越少越靠前，作用类似List下标*/
    int sort() default 0;

    /**
     * 在框架启动 前 执行
     * @return
     */
    boolean before() default true;

    /**
     * 在框架启动 后 执行
     * @return
     */
    boolean after() default false;
}
