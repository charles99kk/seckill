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
		 * ��һ��ִ�н��:1,����ɹ�
		 * �ڶ���ִ�н��:0,�ظ�key
		 * */
		long seckillId = 1000;
		long userPhone = 23412344321L;
		int insertNom = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println("insert Nom ="+insertNom);
	}
	
	@Test
	public void testQueryByIdWithSeckill() {
		/*
		 * state= -1 ԭ�� state     tinyint NOT NULL DEFAULT -1 COMMENT '-1:��Ч 0:�ɹ� 1:�Ѹ��� 2:����',
		 * ����ʱ�趨ΪĬ��-1
		 * ��Ϊ0�����֣�
		 * 1��  NOT NULL DEFAULT 0 COMMENT '-1:��Ч 0:�ɹ� 1:�Ѹ��� 2:����',
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
