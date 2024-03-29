package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	/*
	 * 减库存，
	 * @param seckillId
	 * @param killTime
	 * @return 如果影响行数>1,表示更新的记录行数
	 * */
	//int reduceNumber(long seckillId,Date killTime);
	int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);
	/*
	 * 根据ID查询秒杀对象
	 * @param seckillId
	 * @return
	 * */
	Seckill queryById(long seckillId);
	/*
	 * 根据偏移量查询秒杀商品列表
	 * @param offet
	 * @param limit
	 * @return
	 * */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	//List<Seckill> queryAll(int offset,int limit);
}
