package org.study.rpc.model;

/**
 * @Title: RpcResponse.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午2:07:19
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcResponse {

	private String requestId;
	private Throwable error;
	private Object result;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
