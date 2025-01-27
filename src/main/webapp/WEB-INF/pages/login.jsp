<%@ page import="com.bai.utils.constants.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>图书馆首页</title>
	<link href="<c:url value="<%=Constants.StaticIcon.ICON_PATH%>"/>" rel="shortcut icon"
	      type="image/vnd.microsoft.icon"/>
	<link rel="stylesheet" href="/static/css/bootstrap.min.css">
	<script src="/static/js/js.cookie.js"></script>
	<style>
        #login {
            height: 50%;
            width: 28%;
            margin-left: auto;
            margin-right: auto;
            margin-top: 5%;
            display: block;
            position: center;
        }

        .form-group {
            margin-bottom: 0;
        }

        * {
            padding: 0;
            margin: 0;
        }
	
	</style>
</head>
<body background="/static/img/1583851799.jpg"
      style="background-repeat: no-repeat; background-size: 100% 100%;background-attachment: fixed;opacity: 0.9;">
<%--<c:if test="test=${!empty error}">--%>
<%--    <script>--%>
<%--        alert(${error});--%>
<%--        window.location.href="/tologin";--%>
<%--    </script>--%>
<%--</c:if>--%>
<h2 style="text-align: center; color: white; font-family: '华文行楷'; font-size: 500%">图 书 馆</h2>

<div class="panel panel-default" id="login" style=" border: none !important;background: none !important;">
	<%--	<div style="color: #CCCCCC;left: 350px;position: relative;"><h3><a href="<c:url value="/reader_register.do"/>">注册</a></h3></div>--%>
	<div class="panel-body">
		<div class="form-group">
			<label for="id">账号</label>
			<input type="text" class="form-control" id="id" placeholder="请输入账号">
		</div>
		<div class="form-group">
			<label for="passwd">密码</label>
			<input type="hidden" name="_model" value="admin" id="_model">
			<input type="password" class="form-control" id="passwd" placeholder="请输入密码">
		</div>
		<div class="checkbox text-left">
			<label>
				<input type="checkbox" id="remember">记住密码
			</label>
		</div>
		
		
		<p style="text-align: right;color: red;position: absolute" id="info"></p><br/>
		<button id="loginButton" class="btn btn-primary  btn-block">登陆
		</button>
	</div>
</div>

<script src="/static/js/jquery-3.2.1.js"></script>
<script type="text/javascript">
    $("#id").keyup(
        function () {
            if (isNaN($("#id").val())) {
                $("#info").text("提示:账号只能为数字");
            } else {
                $("#info").text("");
            }
        }
    )

    // 记住登录信息
    function rememberLogin(username, password, checked) {
        Cookies.set('loginStatus', {
            username: username,
            password: password,
            remember: checked
        }, {expires: 30, path: ''})
    }

    // 若选择记住登录信息，则进入页面时设置登录信息
    function setLoginStatus() {
        var loginStatusText = Cookies.get('loginStatus')
        if (loginStatusText) {
            var loginStatus;
            try {
                loginStatus = JSON.parse(loginStatusText);
                $('#id').val(loginStatus.username);
                $('#passwd').val(loginStatus.password);
                $("#remember").prop('checked', true);
            } catch (__) {
            }
        }
    }

    // 设置登录信息
    setLoginStatus();
    $("#loginButton").click(
        function () {
            let id = $("#id").val()
            let password = $("#passwd").val()
            let _model = $("#_model").val()
            let checked = $("#remember").prop("checked")
            if (id == '') {
                $("#info").text("提示:账号不能为空");
            } else if (passwd == '') {
                $("#info").text("提示:密码不能为空");
            } else if (isNaN(id)) {
                $("#info").text("提示:账号必须为数字");
            } else {
                $.ajax({
                    url: "checklogin",
                    type: "POST",
                    dataType: "json",
                    async: true,
                    data: {id: id, password: password, _model},
                    success: data => {
                        if (data.stateCode.trim() === "-1") {
                            $("#info").text("提示:" + data.msg);
                        } else if (data.stateCode.trim() === "0") {
                            $("#info").text("提示:账号或密码错误！");
                        } else {
                            let page;
                            if (data.stateCode.trim() === "1") {
                                $("#info").text("提示:登陆成功，跳转中...");
                                page = "/admin_main.html";
                            } else if (data.stateCode.trim() === "2") {
                                $("#info").text("提示:登陆成功，跳转中...");
                                page = "/reader_admin";
                            }
                            if (checked) {
                                rememberLogin(id, password, checked);
                            } else {
                                Cookies.remove('loginStatus');
                            }
                            window.location.href = page
                        }
                    }
                })
            }

        }
    )

</script>
</body>
</html>
