package org.seckill.exception;

/*
 * �ظ���ɱ�쳣(�������쳣��spring������ʽ���������runtime�쳣��������������쳣����ô���ǲ���ع���)
 * */
public class RepeatKillExeception extends SeckillException{

	public RepeatKillExeception(String message){
		super(message);
	}
	
	public RepeatKillExeception(String message,Throwable cause){
		super(message,cause);
	}
}
