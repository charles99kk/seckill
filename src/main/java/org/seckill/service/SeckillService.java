package org.seckill.service;

import java.util.List;

import javax.swing.text.html.parser.Entity;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/*
 * 业务接口：站在"使用者角"度设计接口
 * 三个方面：方法定义粒度，参数，返回类型(return 类型/exception )友好
 * */
public interface SeckillService {
	/*
	 * 查询所有秒杀记录
	 * @return
	 * */
	List<Seckill> getSeckillList();
	/*查询单个秒杀记录
	 * @param seckillId
	 * @retrun
	 */
	Seckill getById(long seckillId);
	
	/*
	 * 秒杀开启时输出秒杀接口地址，
	 * 否则输出系统时间和秒杀 时间
	 *  @param seckillId
	 *  @retrun
	 * */
	Exposer exportSeckillUrl(long seckillId);
	/*
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
		throws SeckillException,RepeatKillExeception,SeckillCloseException;
}
