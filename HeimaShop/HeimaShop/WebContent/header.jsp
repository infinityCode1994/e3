<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<c:if test="${user==null }">
				<li><a href="${pageContext.request.contextPath }/user?method=clearLoginInfo">登录</a></li>
			</c:if>
			<c:if test="${user!=null }">
				<li style="color : red">欢迎您${user.username }</li>
				<li><a href="${pageContext.request.contextPath }/user?method=Logout">退出</a></li>
			</c:if>
			<li><a href="register.jsp">注册</a></li>
			<li><a href="cart.jsp">购物车</a></li>
			<li><a href="${pageContext.request.contextPath }/product?method=MyOrderList">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath }">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="categoryUl">
					
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group" style="position:relative" >
						<input id="search" type="text" class="form-control" placeholder="Search" onkeyup="showInfo(this)" >
						<div id="serachInfo" onblur="hideInfo(this)" style="display:none;position:absolute;z-index:1000;background:#fff;width:195px;border:1px solid #ccc;">
							
						</div>
					</div>					
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
		<script type="text/javascript">
			$(function(){
				var content="";
				$.post(
						"${pageContext.request.contextPath}/product?method=CategoryList",
						function(data){
							for(var i=0;i<data.length;i++){
								content+="<li><a href='${pageContext.request.contextPath}/product?method=FindProductListByCid&cid="+data[i].cid+"'>"
								+data[i].cname+"</a></li>";
							}
							$("#categoryUl").html(content);
						},
						"json"
				);
			});
			function hideInfo(obj){
				$(obj).css("display","none");
			}
			
			function outFn(obj){
				$(obj).css("background","#fff");
			}
			function overFn(obj){
				$(obj).css("background","#DBEAF9");
			}
			function clickFn(obj){
				$("#search").val($(obj).html());
				$("#serachInfo").css("display","none");
			}
			function showInfo(obj){
				
				
				//alert(obj.value);
			 	var content="";
			 	var word = $(obj).val();
				$.post(
						"${pageContext.request.contextPath}/product?method=findProductNameByword",
						{"word":word},
						function(data){
							for(var i=0;i<data.length;i++){
								content+="<div style='padding:2px;cursor:pointer' onclick='clickFn(this)' onmouseover='overFn(this)' onmouseout='outFn(this)'>"+data[i]+"</div>";
								
							}
							$("#serachInfo").html(content);
							$("#serachInfo").css("display","block");
						},
						"json"
				); 
			}
		</script>
	</nav>
</div>