package org.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @Title: RpcService.java
 * @Description: TODO 指定远程接口
 * @author zhaotf
 * @date 2018年3月18日 下午12:19:26
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
@Target(value = { ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

	public Class<?> value();

}
