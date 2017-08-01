package org.seckill.dao;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	@Resource
	private SuccessKilledDao successKilledDao;
	@Test
	public void testInsertSuccessKilled() {
		/*
		 * 第一次执行结果:1,插入成功
		 * 第二次执行结果:0,重复key
		 * */
		long seckillId = 1000;
		long userPhone = 23412344321L;
		int insertNom = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println("insert Nom ="+insertNom);
	}
	
	@Test
	public void testQueryByIdWithSeckill() {
		/*
		 * state= -1 原因 state     tinyint NOT NULL DEFAULT -1 COMMENT '-1:无效 0:成功 1:已付款 2:发货',
		 * 创建时设定为默认-1
		 * 改为0有两种：
		 * 1）  NOT NULL DEFAULT 0 COMMENT '-1:无效 0:成功 1:已付款 2:发货',
		 * 2)  insert ignore into Success_killed(seckill_id,user_phone,state)
				values(#{seckillId},#{userPhone},0);
		 * */
		long seckillId = 1000;
		long userPhone = 23412344321L;
		SuccessKilled sk= successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
		if(sk != null){
			System.out.println("SuccessKilled="+sk.toString());
			System.out.println("seckill="+sk.getSeckill().toString());
		}
	}

}
