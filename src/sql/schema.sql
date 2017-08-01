--数据库初始化脚本

--连接数据库控制台
mysql -uroot -proot
/*为什么手写DDL
 * 记录每次上线的DDL修改
 * 上线V1.1
ALTER TABLE seckill
DROP INDEX idx_create_time,
ADD index idx_c_s(start_time,create_time);
 **************/

--创建db
CREATE DATABASE seckill;
--使用数据库
use seckill;
--创建秒杀库存表 创建后可用show create table seckill \G查看创建语句
-- 表名和column不可加''会出错，不加的话show create table时会自动显示
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


--初始化数据
insert into 
	seckill(name,number,start_time,end_time)
values
	('1000元秒杀iPhone6',100,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('500元秒杀ipad2',200,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('300元秒杀小米4',300,'2017-07-03 00:00:00','2017-07-04 00:00:00'),
	('200元秒杀魅族',400,'2017-07-03 00:00:00','2017-07-04 00:00:00');
--秒杀成功明细表
--用户登录认证 相关信息
CREATE TABLE success_killed(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT 'ITEM_ID',
user_phone bigint NOT NULL  COMMENT 'phone nomber',
state     tinyint NOT NULL DEFAULT -1 COMMENT  '-1:无效 0:成功 1:已付款 2:发货',
create_time  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
PRIMARY KEY (seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT 'success detail';
--连接数据库控制台
mysql -uroot -proot