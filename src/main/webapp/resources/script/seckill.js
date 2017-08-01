//存放主要交换逻辑js代码
//javascript模块化
//seckill.detail.init(params);
var seckill={
	//封装秒杀相关ajax的url	
	URL:{
		now:function(){
			return '/seckill/time/now';
		}
	},
	//验证手机号 isNaN(phone)数字验证
	validatePhone:function(phone){
		if(phone && phone.length == 11 && !isNaN(phone)){
			return true;
		} else {
			return false;
		}
	},
	countdown:function(seckillId,nowTime,startTime,endTime){
		//时间判断
		var seckillBox = $('#seckill-box');
		if(nowTime > endTime){
			seckillBox.html('秒杀结束！');
		}else if(nowTime < startTime){
			//未开始，计时时间绑定 加一秒 防止计时偏移
			var killTime = new Datee(startTime + 1000);
			
			seckillBox.countdown(killTime,function(event){
				//时间格式
				var format=event.strftime('计时倒计时: %D天 %H时 %M分 %S秒');
				seckillBox.html(format);
			});
		}else{
			seckillBox.html('秒杀开始！');
		}
	},
	//详情页秒杀逻辑
	detail:{
		
		//详情页初始化
		init:function(params){
			console.log("!!!!!!detail");
			//手机验证和登录，计时交互
			//规划交互流程
			//在cookie中查找手机号
			var killPhone = $.cookie('killPhone');

			//验证手机号
			if(!seckill.validatePhone(killPhone)){
				//绑定phone
				//控制输出
				console.log("!!!!!!killPhoneModal");
				var killPhoneModal = $('#killPhoneModal');
				//modal bootstrap组件，调用方法显示弹出层
				killPhoneModal.modal({
					show:true,//show pop layer
					backdrop:'static',//禁止位置关闭
					keyboard:false//关闭键盘事件
				});
				$('#killPhoneBtn').click(function(){
					var inputPhone =$('#killPhoneKey').val();
					console.log('inputPhone:'+inputPhone);
					if(seckill.validatePhone(inputPhone)){
						//电话写入cookie 
						//  {expires:7,path:'/seckill'}有效期7天,在/seckill路径下有效
						$.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
						//刷新页面
						window.location.reload();
					} else{
						//1.隐藏节点，2编辑节点，3显示内容
						$('#killPhoneMessage').hide().html('<laber class="label label-danger">phone number is wrong</laber>').show(300);
					}
				});
				//已经登录
				//计时交互
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				$.get(seckill.URL.now(),{},function(result){
					if(result&&result['success']){
						var nowTime=result['data'];
					}else{
						console.log('result'+result);
					}
				});
			}
		}
	}
		
}