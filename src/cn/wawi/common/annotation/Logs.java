package cn.wawi.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 注解实现日志记录
 * @author 龚亮
 * @date 2015-05-26 09:49:19
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
@Documented
public @interface Logs {
	String des(); //日志描述
}
