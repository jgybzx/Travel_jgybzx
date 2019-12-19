<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="css/webbase.css">
    <link rel="stylesheet" type="text/css" href="css/pages-getOrderInfo.css">

    <title>结算页</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>

<div class="container-fluid">
    <form action="${ctx}/OrderServlet" method="post">
        <%--表单隐藏域，--%>
        <input type="text" name="action" value="submitOrder" hidden>
        <!-- 头部 end -->
        <div class="cart py-container">
            <!--主内容-->
            <div class="checkout py-container">
                <div class="step-tit">

                    <h4 style="font-weight: bold">填写并核对订单信息</h4>
                </div>
                <div class="checkout-steps">
                    <!--收件人信息-->
                    <div class="step-tit">
                        <h5>收件人信息</h5>
                    </div>
                    <div class="step-cont">
                        <div class="addressInfo">
                            <ul class="addr-detail">
                                <li class="addr-item">
                                    <%--收货地址div--%>
                                    <div>
                                        <c:forEach var="address" items="${userAddress}">
                                        <div class="con address"><input type="radio" id="${address.aid}" ${address.isDefault==1?"checked":""} name="addressId" value="1">
                                            ${address.contact} ${address.address} <span>${address.telephone}</span></div>
                                        </c:forEach>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="hr"></div>

                    </div>
                    <div class="hr"></div>
                    <!--支付和送货-->
                    <div class="payshipInfo">
                        <div class="step-tit">
                            <h5>支付方式</h5>
                        </div>
                        <div class="step-cont">
                            <ul class="payType">
                                <li class="selected">微信付款</li>
                            </ul>
                        </div>
                        <div class="hr"></div>
                        <div class="step-tit">
                            <h5>送货清单</h5>
                        </div>
                        <div class="step-cont">
                            <ul class="send-detail">
                                <li>
                                    <div class="sendGoods">
                                        <%--遍历购物车数据中的 cartItmeMap el表达式底层是 entrySet--%>
                                        <c:forEach var="entry" items="${cart.cartItmeMap}" >
                                            <ul class="yui3-g">
                                                <li class="yui3-u-1-6">
                                                    <span><img src="${entry.value.route.rimage}"/></span>
                                                </li>
                                                <li class="yui3-u-7-12">
                                                    <div class="desc">${entry.value.route.rname}</div>
                                                    <div class="seven">7天无理由退货</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="price">￥${entry.value.route.price}</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="num">X${entry.value.count}</div>
                                                </li>
                                                <li class="yui3-u-1-12">
                                                    <div class="exit">有货</div>
                                                </li>
                                            </ul>
                                        </c:forEach>

                                    </div>
                                </li>
                                <li></li>
                                <li></li>
                            </ul>
                        </div>
                        <div class="hr"></div>
                    </div>
                </div>
            </div>
            <div class="clearfix trade">
                <div class="fc-price">
                   <span class="number">${cart.cartItmeMap.size()}</span>件商品，应付金额:　<span class="price">¥${cart.totalPrice}</span>
                </div>
            </div>
            <div class="submit">
                <button class="sui-btn btn-danger btn-xlarge" >提交订单</button>
            </div>
        </div>
    </form>
</div>
<!-- 底部栏位 -->
<!--引入尾部-->
<%@include file="footer.jsp"%>
<script src="js/getOrderInfo.js"></script>
</body>
</html>
