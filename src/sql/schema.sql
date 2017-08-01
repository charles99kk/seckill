--���ݿ��ʼ���ű�

--�������ݿ����̨
mysql -uroot -proot
/*Ϊʲô��дDDL
 * ��¼ÿ�����ߵ�DDL�޸�
 * ����V1.1
ALTER TABLE seckill
DROP INDEX idx_create_time,
ADD index idx_c_s(start_time,create_time);
 **************/

--����db
CREATE DATABASE seckill;
--ʹ�����ݿ�
use seckill;
--������ɱ���� ���������show create table seckill \G�鿴�������
-- ������column���ɼ�''��������ӵĻ�show create tableʱ���Զ���ʾ
CREATE TABLE seckill (
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT 'ITEM_ID',
name varchar(120) NOT NULL  COMMENT 'ITEM_NAME',
number  int NOT  NULL  COMMENT 'ITEM_amount',
start_time  timestamp NOT NULL  COMMENT 'start_time',
end_time  timestamp NOT NULL  COMMENT 'end_time',
create_time  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT 'second kill';


--��ʼ������
insert into 
	seckill(name,number,start_time,end_time)
values
	('1000Ԫ��ɱiPhone6',100,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('500Ԫ��ɱipad2',200,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('300Ԫ��ɱС��4',300,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('200Ԫ��ɱ����',400,'2017-07-03 00:00:00','2017-07-04 00:00:00');
--��ɱ�ɹ���ϸ��
--�û���¼��֤ �����Ϣ
CREATE TABLE success_killed(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT 'ITEM_ID',
user_phone bigint NOT NULL  COMMENT 'phone nomber',
state     tinyint NOT NULL DEFAULT -1 COMMENT  '-1:��Ч 0:�ɹ� 1:�Ѹ��� 2:����',
create_time  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
PRIMARY KEY (seckill_id,user_phone),/*��������*/
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT 'success detail';
--�������ݿ����̨
mysql -uroot -proot