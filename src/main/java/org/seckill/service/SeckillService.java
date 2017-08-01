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
 * ҵ��ӿڣ�վ��"ʹ���߽�"����ƽӿ�
 * �������棺�����������ȣ���������������(return ����/exception )�Ѻ�
 * */
public interface SeckillService {
	/*
	 * ��ѯ������ɱ��¼
	 * @return
	 * */
	List<Seckill> getSeckillList();
	/*��ѯ������ɱ��¼
	 * @param seckillId
	 * @retrun
	 */
	Seckill getById(long seckillId);
	
	/*
	 * ��ɱ����ʱ�����ɱ�ӿڵ�ַ��
	 * �������ϵͳʱ�����ɱ ʱ��
	 *  @param seckillId
	 *  @retrun
	 * */
	Exposer exportSeckillUrl(long seckillId);
	/*
	 * ִ����ɱ����
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
		throws SeckillException,RepeatKillExeception,SeckillCloseException;
}
