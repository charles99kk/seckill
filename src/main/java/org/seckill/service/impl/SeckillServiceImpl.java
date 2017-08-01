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
	
	//ע��Service����
	@Autowired //��Spring ����Dao���͵�ʵ��(Mybatisʵ�ֵ�,)����ע�롣������Ҫ�Լ�new ʵ��
	//����@Resource,@Inject��J2EE�淶��ע��
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	//md5��ֵ�ַ��������ڻ���MD5
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
		//ϵͳ��ǰʱ��
		Date nowTime = new Date();
		if(nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()){
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),
					endTime.getTime());
		}
		//ת���ض��ַ����Ĺ��̣�������
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
	 * ʹ��ע��������񷽷����ŵ㣺
	 *	1�������ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
	 *		ps��ʹ��aop�����������ɿ���������Ҫʹ��ʲô��������������
	 *	2����֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������rpc/http�Ȼ��߰��뵽�����ⲿ��
	 *  	ps����Ϊ��Щ����һ��Ҫ�����뵽��ʮ���룬Ӱ�������ٶȡ�
	 *  3���������еķ�������Ҫ������ֻ��һ���޸Ĳ�����ֻ����������Ҫ������ơ�
	 *  	ps������������ļ�����������<tx:advice aop�����ռ�>ʹ��aop�������񣬲�ͬ���˵�����ϰ�߿��ܻ������Ҫ����ķ����������
	 * */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillExeception, SeckillCloseException {
		if(md5==null|| !md5.equals(getMD5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		//ִ����ɱ�߼�:����� + ��¼������Ϊ
		Date nowTime = new Date();
		try{

			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCount <=0){
				//û�и��¼�¼
				throw new SeckillCloseException("seckill is closed");
			} else {
				//��¼������Ϊ
				int insertCount= successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//Ψһ:insert ignore 
				if(insertCount <=0){
					//�ظ���ɱ
					throw new RepeatKillExeception("seckill repeated");
				}else {
					//��ɱ�ɹ�
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					//return new SeckillExecution(seckillId,1,"��ɱ�ɹ�",successKilled);
					return new  SeckillExecution(seckillId,SeckillStatEnum.SUCCESS);
				}
			}
			
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillExeception e2) {
			throw e2;
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			//���б������쳣 ת��Ϊ�������쳣
			//spring�������roll back
			throw new SeckillException("seckill inner error : "+e.getMessage());
		}
	}

}
