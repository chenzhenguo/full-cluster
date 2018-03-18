package org.study.rpc.model;

import java.io.Serializable;

/**
 * @Title: RpcRequest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午2:05:07
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String requestId;
	private String className;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] parameters;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}
