<%@ page import="com.bai.utils.constants.Constants" %><%--
  Created by IntelliJ IDEA.
  User: 云飘
  Date: 2020/6/2
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>管理主页</title>
	<link href="" rel="shortcut icon"
	      type="img/library.png"/>
	<link rel="stylesheet" href="/static/css/bootstrap.min.css">
	<script src="/static/js/jquery-3.2.1.js"></script>
	<script src="/static/js/bootstrap.min.js"></script>
	<script src="/static/js/js.cookie.js"></script>
</head>
<body background="/static/img/1583851799.jpg"
      style=" background-repeat:no-repeat ;background-size:100% 100%;background-attachment: fixed;">

<jsp:include page="common_header_admin.jsp"/>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					温馨提示
				</h4>
			</div>
			<div class="modal-body">
				使用结束后请安全退出。
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">知道了
				</button>
			</div>
		</div>
	</div>
</div>
<c:if test="${!empty login}">
	<script>
        $(function () {
            $("#myModal").modal({
                show: true
            })
        })
	</script>
</c:if>
</body>
</html>
