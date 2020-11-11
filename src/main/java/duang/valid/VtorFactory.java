package duang.valid;

import duang.exception.DuangException;
import duang.mvc.common.dto.ValidDto;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *验证工厂类，利用hibernate-validator对请求方法里的参数进行验证
 *
 * @author Laotang
 * @since 1.0
 */
public class VtorFactory {

    private static Validator validator;
    private static ExecutableValidator executableValidator;

    private static class SingletonHolder {
        private static final VtorFactory INSTANCE = new VtorFactory();
    }

    private VtorFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        executableValidator = validator.forExecutables();
    }

    public static VtorFactory duang() {
        return VtorFactory.SingletonHolder.INSTANCE;
    }

    /**
     * 验证标记了@Bean的实体类
     *
     * @param bean 实体类
     * @return 验证通过返回true，否则抛出异常
     */
    public <T> T validate(T bean) throws DuangException {
        List<ValidDto> validDtoList = validateBeanOrParams(validator.validate(bean));
        if (ToolsKit.isNotEmpty(validDtoList)) {
            throw new DuangException(ToolsKit.toJsonString(validDtoList), validDtoList);
        }
        return (T)bean;
    }

    public <T> T validateParameters(T object, Method method, Object[] parameterValues, Class<?>... groups) {
        List<ValidDto> validDtoList = validateBeanOrParams(executableValidator.validateParameters(object, method, parameterValues, groups));
        if (ToolsKit.isNotEmpty(validDtoList)) {
            throw new DuangException(ToolsKit.toJsonString(validDtoList), validDtoList);
        }
        return object;
    }

    private List<ValidDto> validateBeanOrParams(Set constraintViolations) {
        if (constraintViolations.isEmpty()) {
            return null;
        }
        List<ValidDto> validDtoList = new ArrayList<>(constraintViolations.size());
        for (Object violationObj : constraintViolations) {
            ConstraintViolation violation = (ConstraintViolation)violationObj;
            String fieldName = violation.getPropertyPath().toString();
            int startIndex = fieldName.indexOf(".");
            if (startIndex > -1) {
                fieldName = fieldName.substring(startIndex+1);
            }
            validDtoList.add(new ValidDto(fieldName, violation.getMessage()));
        }
        return validDtoList;
    }

}
