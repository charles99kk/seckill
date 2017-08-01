package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

//@Component @service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//注入Service依赖
	@Autowired //在Spring 查找Dao类型的实例(Mybatis实现的,)，并注入。不在需要自己new 实例
	//还有@Resource,@Inject等J2EE规范的注解
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	//md5盐值字符串，用于混淆MD5
	private final String slat = "sldijfldkjfpaojj@#(#$sldfj`123";
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		// TODO Auto-generated method stub
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if(seckill == null){
			return new Exposer(false,seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		//系统当前时间
		Date nowTime = new Date();
		if(nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()){
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),
					endTime.getTime());
		}
		//转化特定字符串的过程，不可逆
		String md5 = getMD5(seckillId);//TODO
		return new Exposer(true,md5,seckillId);
	}

	private String getMD5(long seckillId){
		String base = seckillId+"/"+slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	@Override
	@Transactional
	/*
	 * 使用注解控制事务方法的优点：
	 *	1、开发团队达成一致约定，明确标注事务方法的编程风格。
	 *		ps：使用aop管理事务会造成可能遗忘需要使用什么方法命名等问题
	 *	2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作rpc/http等或者剥离到事务外部。
	 *  	ps：因为这些操作一次要几毫秒到几十毫秒，影响事务速度。
	 *  3、不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制。
	 *  	ps：如果在配置文件里配置永久<tx:advice aop命名空间>使用aop控制事务，不同的人的命名习惯可能会给不需要事务的方法添加事务
	 * */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillExeception, SeckillCloseException {
		if(md5==null|| !md5.equals(getMD5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		//执行秒杀逻辑:减库存 + 记录购买行为
		Date nowTime = new Date();
		try{

			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCount <=0){
				//没有更新记录
				throw new SeckillCloseException("seckill is closed");
			} else {
				//记录购买行为
				int insertCount= successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//唯一:insert ignore 
				if(insertCount <=0){
					//重复秒杀
					throw new RepeatKillExeception("seckill repeated");
				}else {
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					//return new SeckillExecution(seckillId,1,"秒杀成功",successKilled);
					return new  SeckillExecution(seckillId,SeckillStatEnum.SUCCESS);
				}
			}
			
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillExeception e2) {
			throw e2;
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			//所有编译期异常 转化为运行期异常
			//spring事务会做roll back
			throw new SeckillException("seckill inner error : "+e.getMessage());
		}
	}

}
