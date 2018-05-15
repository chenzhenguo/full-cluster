package org.jeecgframework.core.aop;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @Title: HiberAspect
 * @Description:实现创建人，创建时间，创建人名称自动注入;修改人,修改时间,修改人名自动注入;
 * @Author: zhaotf
 * @Since:2018年4月3日 下午5:38:18
 */
@Component
public class HiberAspect extends EmptyInterceptor{
	private static final Logger logger = LoggerFactory.getLogger(HiberAspect.class);
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		logger.info("自定义Hiberate拦截器-onSave:{},{},{},{},{}", entity,id,state,propertyNames,types);
//		TSBaseUser currentUser = null;
//		String currentUserIp = null;
//		try {
//			currentUser = ResourceUtil.getSessionUserName();
//			currentUserIp = ResourceUtil.getSessionUserIp();
//		} catch (RuntimeException e) {
//			logger.warn("当前session为空,无法获取用户");
//		}
//		if (currentUser == null) {
//			return true;
//		}
//		try {
//			// 添加数据
//			for (int index = 0; index < propertyNames.length; index++) {
//				/* 找到名为"创建时间"的属性 */
//				if ("createDate".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"创建时间"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = new Date();
//					}
//					continue;
//				}
//				/* 找到名为"创建人"的属性 */
//				else if ("createBy".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"创建人"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUser.getId();
//					}
//					continue;
//				}
//				/* 找到名为"创建人名称"的属性 */
//				else if ("createName".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"创建人名称"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUser.getTrueName();
//					}
//					continue;
//				} else if ("insertip".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUserIp;
//					}
//					continue;
//				} else if ("inserttime".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						// state[index] = DataUtils.timestamptoStrDatetime();
//						state[index] = DataUtils.getDate();
//					}
//					continue;
//				} else if ("insertuser".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUser.getId();
//					}
//					continue;
//				} else if ("updateip".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUserIp;
//					}
//					continue;
//				} else if ("updatetime".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						// state[index] = DataUtils.timestamptoStrDatetime();
//						state[index] = DataUtils.getDate();
//					}
//					continue;
//				} else if ("updateuser".equals(propertyNames[index])) {
//					/* 使用拦截器将对象的"登录ip"属性赋上值 */
//					if (oConvertUtils.isEmpty(state[index])) {
//						state[index] = currentUser.getId();
//					}
//					continue;
//				}
//
//			}
//		} catch (RuntimeException e) {
//			logger.error("", e);
//		}
		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		logger.info("自定义Hiberate拦截器-onSave:{},{},{},{},{},{}", entity,id,currentState,propertyNames,types);
//		TSBaseUser currentUser = null;
//		String currentUserIp = null;
//		try {
//			currentUser = ResourceUtil.getSessionUserName();
//			currentUserIp = ResourceUtil.getSessionUserIp();
//		} catch (RuntimeException e1) {
//			logger.warn("当前session为空,无法获取用户");
//		}
//		if (currentUser == null) {
//			return true;
//		}
//		// 添加数据
//		for (int index = 0; index < propertyNames.length; index++) {
//			/* 找到名为"修改时间"的属性 */
//			if ("updateDate".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"修改时间"属性赋上值 */
//				currentState[index] = new Date();
//				continue;
//			}
//			/* 找到名为"修改人"的属性 */
//			else if ("updateBy".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"修改人"属性赋上值 */
//				currentState[index] = currentUser.getId();
//				continue;
//			}
//			/* 找到名为"修改人名称"的属性 */
//			else if ("updateName".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"修改人名称"属性赋上值 */
//				currentState[index] = currentUser.getTrueName();
//				continue;
//			} else if ("updateip".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"登录ip"属性赋上值 */
//				if (oConvertUtils.isEmpty(currentState[index])) {
//					currentState[index] = currentUserIp;
//				}
//				continue;
//			} else if ("updatetime".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"登录ip"属性赋上值 */
//				if (oConvertUtils.isEmpty(currentState[index])) {
//					// currentState[index] = DataUtils.timestamptoStrDatetime();
//					currentState[index] = DataUtils.getDate();
//				}
//				continue;
//			} else if ("updateuser".equals(propertyNames[index])) {
//				/* 使用拦截器将对象的"登录ip"属性赋上值 */
//				if (oConvertUtils.isEmpty(currentState[index])) {
//					currentState[index] = currentUser.getId();
//				}
//				continue;
//			}
//		}
		return true;
	}
}
