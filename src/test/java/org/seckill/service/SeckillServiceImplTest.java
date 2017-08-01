package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
					   "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	@Test
	public void testGetSeckillList() throws Exception{
		List<Seckill> list= seckillService.getSeckillList();
		logger.info("list={}",list);
 	}

	@Test
	public void testGetById() throws Exception{
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}",seckill);
	}

	@Test
	public void testExportSeckillUrl() throws Exception{
		long id = 1000;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposere={}",exposer.toString());
		/*
		 * exposed=true,
		 * md5=76e96c3b47df23d4239478bf599aae92
		 * */
	}

	@Test
	public void testExecuteSeckill() throws Exception{
		long id = 1000;
		long phone = 13411112222L;
		String md5= "76e96c3b47df23d4239478bf599aae92";
		//SeckillExecution正确返回初次执行，SeckillCloseException，RepeatKillExeception（同id+phone重复执行时）都是正确期待结果
		try{
			SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
			logger.info("SeckillExecution={}",execution);
		}
		 catch (SeckillCloseException e) {
			 logger.error(e.getMessage());
		} catch (RepeatKillExeception e) {
				logger.error(e.getMessage());
		}
		/*13:47:56.706 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Creating a new SqlSession
13:47:56.716 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:56.725 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@934b6cb] will be managed by Spring
13:47:56.733 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>  Preparing: update seckill set number = number - 1 where seckill_id = ? and start_time <= ? and end_time >= ? and number > 0; 
13:47:56.784 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2017-07-10 13:47:56.698(Timestamp), 2017-07-10 13:47:56.698(Timestamp)
13:47:56.855 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 1
13:47:56.856 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:56.856 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1] from current transaction
13:47:56.856 [main] DEBUG o.s.d.S.insertSuccessKilled - ==>  Preparing: insert ignore into Success_killed(seckill_id,user_phone,state) values(?,?,0); 
13:47:56.858 [main] DEBUG o.s.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 13411112222(Long)
13:47:56.970 [main] DEBUG o.s.d.S.insertSuccessKilled - <==    Updates: 1
13:47:56.980 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:56.981 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1] from current transaction
13:47:56.984 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==>  Preparing: select sk.seckill_id, sk.user_phone, sk.state, sk.create_time, s.seckill_id "seckill.seckill_id", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" from Success_killed sk inner join seckill s on sk.seckill_id =s.seckill_id where sk.seckill_id = ? and sk.user_phone = ?; 
13:47:56.984 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==> Parameters: 1000(Long), 13411112222(Long)
13:47:57.013 [main] DEBUG o.s.d.S.queryByIdWithSeckill - <==      Total: 1
13:47:57.022 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:57.023 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:57.023 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:57.023 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2049a9c1]
13:47:57.077 [main] INFO  o.s.service.SeckillServiceImplTest - SeckillExecution=org.seckill.dto.SeckillExecution@7d898981
*/
	}
	
	@Test
	//结合3,4组成逻辑测试方法
	//测试代码完整逻辑，注意可重复执行
	public void testSeckillLogic() throws Exception{
		long id = 1000;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if(exposer.isExposed()){
			logger.info("exposere={}",exposer.toString());
			long phone = 13411112222L;
			String md5= exposer.getMd5();
			//SeckillExecution正确返回初次执行，SeckillCloseException，RepeatKillExeception都是正确期待结果
			try{
				SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
				logger.info("SeckillExecution={}",execution);
			}
			 catch (SeckillCloseException e) {
				 logger.error(e.getMessage());
			} catch (RepeatKillExeception e) {
					logger.error(e.getMessage());
			}
		}else {
			//秒杀未开启
			logger.warn("exposer={}",exposer);
		}
	}

}
