<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>注册</title>
    <link rel="stylesheet" href="css/register.css">
</head>
<body>
<!--引入头部-->
<jsp:include page="header.jsp"></jsp:include>
<!-- 头部 end -->
<div class="rg_layout">
    <div class="rg_form clearfix">
        <%--左侧--%>
        <div class="rg_form_left">
            <p>新用户注册</p>
            <p>USER REGISTER</p>
        </div>
        <div class="rg_form_center">
            <!--注册表单-->
            <form id="registerForm" action="${pageContext.request.contextPath}/UserServlet" method="post">
                <!--提交处理请求的标识符-->
                <input type="hidden" name="action" value="register">
                <table style="margin-top: 25px;width: 558px">
                    <tr>
                        <td class="td_left">
                            <label for="username">用户名</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="username" name="username" placeholder="请输入账号"
                                   onblur="ifNameExistAjax()">
                            <span id="userInfo" style="font-size:10px"></span>
                        </td>
                        <%--Ajax 异步请求验证账号是否存在--%>
                        <script>
                            function ifNameExistAjax() {
                                //    获取数据，ajax发送请求，接受返回信息，显示返回信息
                                var usernameVal = $("#username").val();
                                if (usernameVal == "") {
                                    $("#userInfo").html("<font color='red'>用户名不能为空✘</font>");
                                    return;
                                }
                                //发送数据,由于要发送到servlet，所以要指定action,所以 参数有俩，需要使用json
                                var params = {"usernameVal": usernameVal, "action": "ifNameExistAjax"}

                                var $url = "${pageContext.request.contextPath}/UserServlet";
                                $.post($url, params, function (resultInfo) {
                                    /*{flag: true, errorMsg: "用户名可用", data: null}*/
                                    console.log(resultInfo);
                                    if (resultInfo.flag) {
                                        $("#userInfo").html("<font color='green'>" + resultInfo.data + "✔</font>");//绿色输出可用
                                    } else {
                                        $("#userInfo").html("<font color='red'>" + resultInfo.errorMsg + "✘</font>");//红色输出已存在
                                    }
                                }, "json")
                            }
                        </script>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="telephone">手机号</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="telephone" name="telephone" placeholder="请输入您的手机号"
                                   onblur="ifPhoneExistAjax()">
                            <span id="telephoneInfo" style="font-size:10px"></span>
                        </td>
                        <script>
                            function ifPhoneExistAjax() {
                                var telephoneVal = $("#telephone").val();
                                var reg = /1[3584]\d{9}/;
                                if (telephoneVal == "") {
                                    $("#telephoneInfo").html("<font color='red'>手机号不能为空✘</font>");
                                    return;
                                }/*else if(reg.test(telephoneVal)){
                                    $("#telephoneInfo").html("<font color='red'>手机号不足11位✘</font>");
                                    return ;
                                }*/
                                var $url = "${pageContext.request.contextPath}/UserServlet";
                                var params = {"telephoneVal": telephoneVal, "action": "ifPhoneExistAjax"}

                                $.post($url, params, function (resultInfo) {
                                    console.log(resultInfo);
                                    if (resultInfo.flag) {
                                        $("#telephoneInfo").html("<font color='green'>" + resultInfo.data +
                                            "✔</font>");//绿色输出可用
                                    } else {
                                        $("#telephoneInfo").html("<font color='red'>" + resultInfo.errorMsg + "✘</font>");//红色输出已存在
                                    }
                                }, "json")
                            }
                        </script>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="password">密码</label>
                        </td>
                        <td class="td_right">
                            <input type="password" id="password" name="password" placeholder="请输入密码" onblur="isNull()">
                            <span id="passwordInfo" style="font-size:10px"></span>
                            <script>
                                function isNull() {
                                    if ($("#password").val() == "") {
                                        $("#passwordInfo").html("<font color='red'>密码不能为空✘</font>");
                                        return;
                                    } else {
                                        $("#passwordInfo").html("<font color='green'>✔</font>");
                                    }
                                }
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="smsCode">验证码</label>
                        </td>
                        <td class="td_right check">
                            <input type="text" id="smsCode" name="smsCode" class="check" placeholder="请输入验证码">
                            <a href="javaScript:void(0)" onclick="senCode()">发送手机验证码</a>
                            <script>
                                function senCode() {

                                    //发送手机验证码，异步请求
                                    var phone = $("#telephone").val();
                                    var $url = "${pageContext.request.contextPath}/UserServlet";
                                    var params = {"phone": phone, "action": "sendCode"}
                                    alert(phone)
                                    $.post($url, params, function (resultInfo) {
                                        if (resultInfo.flag) {
                                            $("#msg").html(resultInfo.data);
                                        } else {
                                            $("#msg").html(resultInfo.errorMsg);
                                        }

                                    }, "json")
                                }
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                        </td>
                        <td class="td_right check">
                            <input type="submit" class="submit" value="注册">
                            <%--错误提示展示位置--%>
                            <span id="msg" style="color: red;">${msg}</span>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <%--右侧--%>
        <div class="rg_form_right">
            <p>
                已有账号？
                <a href="javascript:$('#loginBtn').click()">立即登录</a>
            </p>
        </div>
    </div>
</div>
<!--引入尾部-->
<jsp:include page="footer.jsp"></jsp:include>


</body>
</html>
