package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/*
 * ����spring��junit���ϣ�
 * junit����ʱ����springIOC����
 * spring-test(SpringJUnit4ClassRunner.class)
 * junit(@RunWith)
 * */
@RunWith(SpringJUnit4ClassRunner.class)
//����junit spring�����ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//ע��Daoʵ�������� @Resource���spring�����в���SeckillDao���͵�ʵ���ಢע�뵽seckillDao
	@Resource
	private SeckillDao seckillDao;
	@Test
	public void testQueryById() throws Exception{
		long id = 1000;
		Seckill seckill=seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill.toString());
		/*
		 * 1000Ԫ��ɱiPhone6
			Seckill [seckillId=1000, name=1000Ԫ��ɱiPhone6, number=100, startTime=Mon Jul 03 00:00:00 CST 2017
			, endTime=Tue Jul 04 00:00:00 CST 2017, createTime=Tue Jul 04 10:14:41 CST 2017]
		 * */
	}

	@Test
	public void testQueryAll() throws Exception{
		/* junit����
		 * org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
		 * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
		 * ԭ��List<Seckill> queryAll(int offset,int limit);
		 *	javaû�б����βεĵļ�¼��queryAll(int offset,int limit)-��queryAll(arg0,arg1)
		 *	limit #{offset},#{limit}���޷��ҵ�offset ��limit
		 *	��mybatis�����Ҫ��ʾ��������Ĵ�������Ҫ��ע��@Param��ָ���������������һ���������ǲ���ָ����
		 *	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
		 *
		 */
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		for(Seckill kill: seckills){
			System.out.println(kill.toString());
		}
	}
	@Test
	public void testReduceNumber() throws Exception{
		/*
		 * 16:26:41.616 [main] DEBUG o.m.s.t.SpringManagedTransaction - 
		 * JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@304bb45b] will not be managed by Spring
		 * ���JDBC Connectionû�б�spring�йܣ��Ǵ�c3p0�õ���
		 * 
		 update seckill set number = number - 1 
		 where seckill_id = ? 
		 and start_time <= ? 
		 and end_time >= ? 
		 and number > 0; 
		 Parameters: 1000(Long), 2017-07-05 16:26:41.84(Timestamp), 2017-07-05 16:26:41.84(Timestamp)
		 DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 0
		 updateCount= 0Wed Jul 05 16:26:41 CST 2017
		 * */
		Date killTime =new Date();
		int updateCount =seckillDao.reduceNumber(1000, killTime);
		System.out.println("updateCount= "+updateCount+killTime);
	}

}
