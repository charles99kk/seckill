package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	/*
	 * 插入明细，可过滤重复
	 * @param seckillID
	 * @param userPhone
	 * @return 插入的行数
	 * */
	int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);
	/*
	 * 根据id查询SuccessKilled并携带秒杀产品对象实体
	 * @param seckillID
	 * @return
	 * */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);
}
