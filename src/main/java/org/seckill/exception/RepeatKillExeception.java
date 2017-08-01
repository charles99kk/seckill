package org.seckill.exception;

/*
 * 重复秒杀异常(运行期异常，spring的声明式事务处理的是runtime异常，如果不是这类异常，那么他是不会回滚的)
 * */
public class RepeatKillExeception extends SeckillException{

	public RepeatKillExeception(String message){
		super(message);
	}
	
	public RepeatKillExeception(String message,Throwable cause){
		super(message,cause);
	}
}
