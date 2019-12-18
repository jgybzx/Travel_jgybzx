<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>购物车列表</title>


</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!--引入头部-->
<jsp:include page="header.jsp"/>
<div class="container">
    <%--如果购物车中没有数据应该显示以下内容--%>
    <c:if test="${empty cart.cartItmeMap}">
        <div class="row" style="margin: 100px 200px;text-align: center">
            购物车内暂时没有商品，返回首页
        </div>
    </c:if>
    <%--商品展示div--%>
    <c:if test="${not empty cart.cartItmeMap}">
        <div class="row">
            <div style="margin:0 auto; margin-top:20px">
                <div style="font-weight: bold;font-size: 15px;margin-bottom: 10px"><%--商品数量：3--%></div>
                <table class="table">
                    <tbody>
                    <tr bgcolor="#f5f5f5" class="table-bordered">
                        <th>图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                        <th>操作</th>
                    </tr>
                        <%--
                                此处要循环遍历，购物车中的cartItmeMap，el表达式，对map的遍历底层是
                                entrySet, entrySet.getKey() entrySet.getValue();
                                所以 entrySet.getValue() ==》 ${entry.value}
                                在购物车中 map中的 value  就是 cartItem
                                Cart:  cartItmeMap  ,totalPrice
                                cartItem ： route  count  subTotal

                        --%>
                    <c:forEach items="${cart.cartItmeMap}" var="entry">
                        <tr class="table-bordered">
                            <td width="180" width="40%">
                                <input type="hidden" name="id" value="22">
                                <img src="${entry.value.route.rimage}" width="170" height="100">
                            </td>
                            <td width="30%">
                                <a target="_blank"> ${entry.value.route.rname}</a>
                            </td>
                            <td width="10%"><%--单价--%>
                                ￥ ${entry.value.route.price}
                            </td>
                            <td width="14%">
                                × ${entry.value.count}
                            </td>
                            <td width="15%">
                                <span class="subtotal">￥${entry.value.subTotal}</span>
                            </td>
                            <td>
                                <%--注意此处需要传递一个参数，而不是在方法中获取，因为在方法中 获取的rid，永远是遍历的最后一个--%>
                                <a href="javascript:void(0);" onclick="removeCartItem(${entry.value.route.rid})" class="delete">删除</a>
                            </td>

                        </tr>
                    </c:forEach>
                    <script>
                        function removeCartItem(rid) {
                        <%--function removeCartItem() {  传递参数，每次删除的都会是最后一个--%>
                            /*如果确认删除，直接跳转到servlet，传递需要删除哪个说商品的记录  rid*/
                            if (confirm("是否删除")) {

                                location.href = "${ctx}/CartServlet?action=removeCartItemByRid&rid="+rid;
                            }
                        }
                    </script>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <%--结算div--%>
    <div>
        <div style="text-align:right;">
            商品金额: <strong style="color:#ff6600;">￥${cart.totalPrice}元</strong>
        </div>
        <div style="text-align:right;margin-top:10px;margin-bottom:10px;">
            <%--点击结算跳转到 orderServlet--%>
            <a href="${ctx}/OrderServlet?action=submitCart">
                <input type="button" width="100" value="结算" name="submit" border="0" style="background-color: #ea4a36;
						height:45px;width:120px;color:white;font-size: 15px">
            </a>
        </div>
    </div>

</div>
<!--引入尾部-->
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
