package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/*
 * 封装秒杀执行结果*/
public class SeckillExecution {
	private long seckillId;
	/*此处不做枚举类型的原因:会把这个对象转换为json,通过ajax返回给前端浏览器。
	 * 默认的json的类库在转换枚举时有问题。
	 * (可以做一个把枚举类型转换为json的transfer，不过比较复杂，此处不使用此方法。
	*/
	//秒杀结果状态
	private int state;
	//状态表示
	private String stateInfo;
	//秒杀成功对象
	private SuccessKilled successKilled;

	public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
	}

	
	//public SeckillExecution(long seckillId, int state, String stateInfo, SuccessKilled successKilled) {
	public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	public long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}
	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
}
