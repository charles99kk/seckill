package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/*
 * ��װ��ɱִ�н��*/
public class SeckillExecution {
	private long seckillId;
	/*�˴�����ö�����͵�ԭ��:����������ת��Ϊjson,ͨ��ajax���ظ�ǰ���������
	 * Ĭ�ϵ�json�������ת��ö��ʱ�����⡣
	 * (������һ����ö������ת��Ϊjson��transfer�������Ƚϸ��ӣ��˴���ʹ�ô˷�����
	*/
	//��ɱ���״̬
	private int state;
	//״̬��ʾ
	private String stateInfo;
	//��ɱ�ɹ�����
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
