package org.seckill.dto;

//<T>泛型类型
//所有ajax请求返回类型，封装json结果
public class SeckillResult<T> {

	private boolean success;
	//泛型类型的数据
	private T data;
	private String error;
	public SeckillResult(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}
	public SeckillResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
