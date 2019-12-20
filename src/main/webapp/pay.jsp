<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/webbase.css">
    <link rel="stylesheet" href="css/pages-weixinpay.css">
    <title>微信支付</title>

</head>
<body>
<script src="js/qrious.js"></script>
<!--引入头部-->
<jsp:include page="header.jsp"></jsp:include>
<div class="container-fluid">
    <div class="cart py-container">
        <!--主内容-->
        <div class="checkout py-container  pay">
            <div class="checkout-tit">
                <h4 class="fl tit-txt"><span class="success-icon"></span><span class="success-info">订单提交成功，请您及时付款！订单号：${oid}</span>
                </h4>
                <span class="fr"><em class="sui-lead">应付金额：</em><em class="orange money">￥${totalPrice}</em>元</span>
                <div class="clearfix"></div>
            </div>
            <div class="checkout-steps">
                <div class="fl weixin">微信支付</div>
                <div class="fl sao">
                    <p hidden class="red" style="padding-bottom: 40px">二维码已过期，刷新页面重新获取二维码。</p>
                    <div class="fl code">
                        <img id="codeImage" alt="">
                        <script>
                            window.onload = function () {
                                var url = "${payUrl}";
                                new QRious({
                                    element: document.getElementById("codeImage"),
                                    value: url,
                                    size: "200"
                                });
                                
                                /*设置定时器，访问数据库，订单状态*/
                                setInterval(sheckOrderState,1000);
                            }
                            function sheckOrderState() {
                               var url = "${pageContext.request.contextPath}/PayServlet";
                               /*参数是订单号，*/
                               var parmas ="action=sheckOrderState&oid=${oid}";
                               $.post(url,parmas,function (data) {
                                   //返回为1，表示状态已修改，支付成功,否则，反之
                                   if(data==1){
                                       //页面跳转
                                       location.href = "${pageContext.request.contextPath}/pay_success.jsp?oid=${oid}";
                                   }
                               },"text")
                            }

                        </script>
                        <div class="saosao">
                            <p>请使用微信扫一扫</p>
                            <p>扫描二维码支付</p>
                        </div>
                    </div>
                    <div class="fl"
                         style="background:url(./img/phone-bg.png) no-repeat;width:350px;height:400px;margin-left:40px">

                    </div>

                </div>
                <div class="clearfix"></div>
            </div>
        </div>

    </div>
</div>
<!--引入尾部-->
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
