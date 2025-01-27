<%@ page import="com.bai.utils.constants.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>${readercard.username}的主页</title>
	<link href="<c:url value="<%=Constants.StaticIcon.ICON_PATH%>"/>" rel="shortcut icon"
	      type="image/vnd.microsoft.icon"/>
	<link rel="stylesheet" href="<c:url value="/static/css/bootstrap.min.css"/>">
	<script src="<c:url value="/static/js/jquery-3.2.1.js"/>"></script>
	<script src="<c:url value="/static/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/static/js/js.cookie.js"/>"></script>

</head>
<body background="<c:url value="/static/img/1583851799.jpg"/>" style=" background-repeat:no-repeat ;
background-size:100% 100%;
background-attachment: fixed;">

<c:set var="current" value="2" scope="request"/>
<jsp:include page="common_header_reader.jsp"/>

<div class="col-xs-5 col-md-offset-3">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				信息修改
			</h3>
		</div>
		<div class="panel-body">
			<form action="reader_edit_do_r.html" method="post" id="edit">
				<div class="input-group">
					<span class="input-group-addon">读者证号</span>
					<input type="text" readonly="readonly" class="form-control" name="readerId" id="readerId"
					       value="${readerinfo.readerId}">
				</div>
				<div class="input-group">
					<span class="input-group-addon">姓名</span>
					<input type="text" class="form-control" name="name" id="name" value="${readerinfo.name}">
				</div>
				<div class="input-group">
					<span class="input-group-addon">性别</span>
					<input type="text" class="form-control" name="sex" id="sex" value="${readerinfo.sex}">
				</div>
				<div class="input-group">
					<span class="input-group-addon">生日</span>
					<input type="date" class="form-control" name="birth" id="birth" value="${readerinfo.birth}">
				</div>
				<div class="input-group">
					<span class="input-group-addon">地址</span>
					<input type="text" class="form-control" name="address" id="address" value="${readerinfo.address}">
				</div>
				<div class="input-group">
					<span class="input-group-addon">电话</span>
					<input type="text" class="form-control" name="phone" id="phone" value="${readerinfo.phone}">
				</div>
				<br/>
				<input type="submit" value="确定" class="btn btn-success btn-sm" class="text-left">
				<script>
                    $("#edit").submit(function () {
                        if ($("#name").val() == '' || $("#sex").val() == '' || $("#birth").val() == '' || $("#address").val() == '' || $("#phone").val() == '') {
                            alert("请填入完整图书信息！");
                            return false;
                        }
                    })
				</script>
			</form>
		</div>
	</div>
</div>
</body>
</html>
