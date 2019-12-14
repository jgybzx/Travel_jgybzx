<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--bootstrap--%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/common.css">
<script src="js/jquery-3.3.1.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/getParameter.js"></script>
<%--往域中放入一个键值对  key的名称是ctx 值是动态值 以后使用动态项目名称 ${ctx} 也可以 --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!-- 头部 start -->
<header id="header">
    <%--广告--%>
    <div class="top_banner">
        <img src="images/top_banner.jpg" alt="">
    </div>
    <%--右侧按钮--%>
    <div class="shortcut">
        <!-- 未登录状态 -->
        <c:if test="${empty loginUser}"><%--判断session中的loginUser是否为空，如果是空的，显示此div--%>
            <div class="login_out">
                <a id="loginBtn" data-toggle="modal" data-target="#loginModel" style="cursor: pointer;">登录</a>
                <a href="register.jsp" style="cursor: pointer;">注册</a>

            </div>
        </c:if>
        <c:if test="${not empty loginUser}"><%--如果不为空，则显示 欢迎**--%>
            <div class="login">
                <span>欢迎回来：<font color="#48d1cc" size="3px">${loginUser.username}&nbsp;&nbsp;&nbsp;&nbsp;</font></span>
                <a href="${ctx}/UserServlet?action=findById" class="collection" >个人中心</a><%--当点击个人中心的时候，应该拿到user对象--%>
                <a href="cart.jsp" class="collection">购物车</a>
                <a href="javascript:void(0)" onclick="loginout()">退出</a>
                <script>
                    function loginout(){
                        //询问框
                        var flag = confirm("您是否要退出?");
                        if(flag){//true 确定退出
                            //异步 $.post()   同步  location.href="";
                            location.href="${ctx}/UserServlet?action=loginout";
                        }
                    }
                </script>
            </div>
        </c:if>
        <!-- 登录状态 -->

    </div>
    <%--搜索框--%>
    <div class="header_wrap">
        <div class="topbar">
            <div class="logo">
                <a href="/"><img src="images/logo.jpg" alt=""></a>
            </div>
            <div class="search">
                <input id="rname" name="rname" type="text" placeholder="请输入路线名称" class="search_input" value="${rname}"
                       autocomplete="off">
                <a href="javascript:void(0);" onclick="searchClick()" class="search-button">搜索</a>
            </div>
            <div class="hottel">
                <div class="hot_pic">
                    <img src="images/hot_tel.jpg" alt="">
                </div>
                <div class="hot_tel">
                    <p class="hot_time">客服热线(9:00-6:00)</p>
                    <p class="hot_num">400-618-9090</p>
                </div>
            </div>
        </div>
    </div>
</header>
<!-- 头部 end -->
<!-- 首页导航 -->
<div class="navitem">
    <ul class="nav" id="categoryUI">
        <li class="nav-active"><a href="index.jsp">首页</a></li>
        <li><a href="route_list.jsp">周边游</a></li>
        <li><a href="route_list.jsp">山水游</a></li>
        <li><a href="route_list.jsp">古镇游</a></li>
        <li><a href="route_list.jsp">出境游</a></li>
        <li><a href="route_list.jsp">国内游</a></li>
        <li><a href="route_list.jsp">港澳游</a></li>
        <li><a href="route_list.jsp">台湾游</a></li>
        <li><a href="route_list.jsp">5A景点游</a></li>
        <li><a href="route_list.jsp">全球自由行</a></li>
    </ul>
</div>
<!-- 登录模态框 -->
<div class="modal fade" id="loginModel" tabindex="-1" role="dialog" aria-labelledby="loginModelLable">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <%--头部--%>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="loginModelLable">
                    <ul id="myTab" class="nav nav-tabs" style="width: auto">
                        <li class="active">
                            <a href="#pwdReg" data-toggle="tab">
                                密码登录
                            </a>
                        </li>
                        <li><a href="#telReg" data-toggle="tab">短信登录</a></li>
                    </ul>
                    <%--错误提示框--%>
                    <span id="loginErrorMsg" style="color: red;"></span>
                </h4>

            </div>
            <%--内容--%>
            <div id="myTabContent" class="tab-content">
                <%--密码登录--%>
                <div class="tab-pane fade in active" id="pwdReg">
                    <form id="pwdLoginForm" action="#" method="post">
                        <input type="hidden" name="action" value="pwdLogin">
                        <div class="modal-body">
                            <div class="form-group">
                                <label>用户名</label>
                                <input type="text" class="form-control" id="login_username" name="username"
                                       placeholder="请输入用户名">
                            </div>
                            <div class="form-group">
                                <label>密码</label>
                                <input type="password" class="form-control" id="login_password" name="password"
                                       placeholder="请输入密码">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" id="pwdLogin" class="btn btn-primary" value="登录"/>
                        </div>
                        <script>
                            $(function () {
                                $("#pwdLogin").click(function () {
                                    //获取表单数据
                                    var params = $("#pwdLoginForm").serialize();
                                    var url = "${pageContext.request.contextPath}/UserServlet";
                                    $.post(url, params, function (resultInfo) {
                                        if (resultInfo.flag) {
                                            // alert("登陆成功");
                                            //登陆成功，直接刷新
                                            location.reload();
                                        } else {
                                            // alert("登陆失败")
                                            //登陆失败，取出失败信息，显示在错误提示span中
                                            $("#loginErrorMsg").html(resultInfo.errorMsg);
                                        }
                                    }, "json")
                                    // alert(params);
                                })
                            })
                        </script>
                    </form>
                </div>
                <%--短信登录--%>
                <div class="tab-pane fade" id="telReg">
                    <form id="telLoginForm" method="post" action="#">
                        <input type="hidden" name="action" value="telLogin">
                        <div class="modal-body">
                            <div class="form-group">
                                <label>手机号</label>
                                <input type="text" class="form-control" name="telephone" id="login_telephone"
                                       placeholder="请输入手机号">
                            </div>
                            <div class="form-group">
                                <label>手机验证码</label>
                                <input type="text" class="form-control" id="login_check" name="smsCode"
                                       placeholder="请输入手机验证码">
                            </div>
                            <a href="javaScript:void(0)" id="login_sendSmsCode">发送手机验证码</a>
                        </div>
                        <script>
                            $(function () {
                                //动态绑定发送手机验证码，href="javaScript:void(0) 阻止链接
                                $("#login_sendSmsCode").click(function () {
                                    // alert("绑定")
                                    //1。获得表单手机号数据
                                    let login_telephone =  $("#login_telephone").val();
                                    //2。发送ajax请求
                                    let url = "${ctx}/UserServlet";
                                   let params = {"login_telephone":login_telephone,"action":"phoneSendCode"}
                                    $.post(url,params,function (data) {
                                        if(data=="1"){//表示发送成功
                                            alert("验证码发送成功")
                                        }
                                    })
                                })
                            })
                        </script>
                        <div class="modal-footer">
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" class="btn btn-primary" id="telLogin" value="登录"/>
                        </div>
                        <script>
                            //登陆ajax，需要需要传递的参数  有  action:telLogin，phone，和code
                            $(function () {
                                $("#telLogin").click(function () {
                                    //1.获取表单数据
                                    let params = $("#telLoginForm").serialize();
                                    let url = "${ctx}/UserServlet";
                                    $.post(url,params,function (resultInfo) {
                                        if(resultInfo.flag){
                                            location.reload();
                                        }else{
                                            $("#loginErrorMsg").html(resultInfo.errorMsg);
                                        }
                                    },"json")
                                })
                            })
                            //1.获得表单数据
                        </script>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
