<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入jstl -->
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html>
   <head>
      <title>秒杀列表页</title>
	<%@include file="common/head.jsp" %>
   </head>
   <body>
   		<!-- 頁面顯示部分 :推荐放入一个div中-->
   		<div class="container">
   			<div class="panel panel-default">
   				<div class="panel-heading text-center">
   					<h2>秒杀列表</h2>
   				</div>
   				<div class="panel-body">
   					<table class="table table-hover">
   						<thead>
   							<tr>
   								<th>名称</th>
   								<th>库存</th>
   								<th>开始时间</th>
   								<th>结束时间</th>
   								<th>创建时间</th>
   								<th>详情页</th>
   							</tr>
   						</thead>
   						<tbody>
   							<!-- 通过标签迭代:list -->
							<c:forEach var ="sk" items="${list}">
   							  <tr>
   								<td>${sk.name}</td>
   								<td>${sk.number}</td>
   								<td>
   									<fmt:formatDate value = "${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
   								</td>
   								<td>
   									<fmt:formatDate value = "${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
   								</td>
   								<td>
   									<fmt:formatDate value = "${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
   								</td>
   								<td>
   									<a class="btn btn-info" href="/seckill/seckill/${sk.seckillId}/detail" target="_blank">link</a>
   								</td>
   							  </tr>
   							</c:forEach>
   						</tbody>
   					</table>
   				</div>
   			</div>
   		</div>
   </body> 
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
 
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
