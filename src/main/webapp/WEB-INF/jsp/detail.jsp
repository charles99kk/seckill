<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html>
   <head>
      <title>秒杀詳情</title>
	<%@include file="common/head.jsp" %>
   </head>
   <body>
   <!-- 頁面顯示部分 :推荐放入一个div中-->
   		<div class="container">
   			<div class="panel panel-default">
   				<div class="panel-heading text-center">
   					<h2>秒杀详情</h2>
   					<div class="panel-heading">${seckill.name}</div>
   				</div>
   				<div class="panel-body">
					<!-- 开发交互时补全 -->
						<h2 class="text-danger">
							<!-- time icon -->
							<span class="glyphicon glyphicon-time"></span>
							<!-- time count -->
							<span class="glyphicon" id="seckill-box"></span>
						</h2>
   				</div>
   			</div>
   		</div>
   		<!-- pop layer :in put phone number ,fade 隐藏-->
   		<div id="killPhoneModal" class="modal fade">
   			<div class="modal-dialog">
   			  <div class="modal-content">
 				<div class="modal-header">
 				  <h3 class="modal-title text-center">
 				  	<span class="glyphicon glyphicon-phone"></span>
 				  </h3>
 				</div>
 				<div class="modal-body">
 				  <div class="row">
 				  	<div class="col-xs-8 col-xs-offset-2">
 				  	  <input type="text" name="killPhone" id="killPhoneKey"
 				  	   placeholder="in put phone number" class="form-control">
 				  	</div>
 				  </div>
 				</div>
 				<div class="modal-footer">
 				  <!-- 验证信息 -->
 				  <span id="killPhoneMessage" jclass="glphicon"></span>
 				  <button type="button" id="killPhoneBtn" class="btn btn-success">
 				    <span class="glyphicon glyphicon-phone"></span>
 				    submit
 				  </button>
 				  </div>
 				</div>				  
   			  </div>
   			</div>
   		</div>
   </body> 
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- 使用CDN获取公共js http://www.bootcdn.cn
	使用CDN原因:使用可靠的CDN比发布到项目里更可靠(使用方式搜索 jquery-count ，把script标签复制到页面)
 -->
 <!-- jquery cookie 操作plugin -->
 <script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<!-- 搜索jquery-count得到 -->
<!-- jquery countDown 倒计时plugin -->
<script src="https://cdn.bootcss.com/jquery-countdown/2.0.1/jquery.countdown.min.js"></script>
<!-- 开始编写交互逻辑
 注意： text="text/javascript"/> 浏览器会不加载 虽然HTML语法是对的，js有特殊性必须写成如下-->
<script src="/seckill/resources/script/seckill.js" type="text/javascript"></script>
<script text="text/javascript">
$(function(){
	console.log("!!!!!!detail.jsp");
	//使用EL表达式传入参数
	seckill.detail.init({
		seckillId : ${seckill.seckillId},
		startTime : ${seckill.startTime.time},//毫秒
		endTime : ${seckill.endTime.time}
	});
});
</script>
</html>