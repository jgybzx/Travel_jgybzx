<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>线路列表</title>
    <link rel="stylesheet" href="css/search.css">
</head>
<body>
<!--引入头部-->

<jsp:include page="header.jsp"></jsp:include>
<div class="page_one">
    <div class="contant">
        <div class="crumbs">
            <img src="images/search.png" alt="">
            <p>黑马旅行><span>搜索结果</span></p>
        </div>
        <div class="xinxi clearfix">
            <%--线路列表 start--%>
            <div class="left">
                <div class="header">
                    <span>商品信息</span>
                    <span class="jg">价格</span>
                </div>
                <c:forEach items="${pageBean.data}" var="data" >
                    <ul>
                        <li>
                            <div class="img"><img src="${data.rimage}" width="300px" alt=""></div>
                            <div class="text1">
                                <p>${data.rname}</p>
                                <br/>
                                <p>${data.routeIntroduce}</p>
                            </div>
                            <div class="price">
                                <p class="price_num">
                                    <span>&yen;</span>
                                    <span>${data.price}</span>
                                    <span>起</span>
                                </p>
                                <p><a href="route_detail.jsp">查看详情</a></p>
                            </div>
                        </li>
                    </ul>
                </c:forEach>

                <div class="page_num_inf">
                    <i></i> 共
                    <span>${pageBean.totalPage}</span>页<span>${pageBean.totalCount}</span>条
                </div>
                <div class="pageNum">
                    <ul>
                        <li><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=1&pageSize=5&cid=${cid}">首页</a></li>
                        <li class="threeword"><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=${pageBean.pageNumber-1}&pageSize=5&cid=${cid}">${num}>上一页</a></li>
                        <%--
                            类比百度，每一行就只有10个页码，如果直接从   1到 totalPage 遍历出来，显然不合理，
                            所以我们也需要控制一下，每一行显示的个数，当前页前 有 5 个  当前页后 有4个，加起来一共  10个
                            所以 forEach 的begin和end，就不能从 1 开始了，需要 一个动态的数据，也就是
                            begin = pageNumber-5
                            end = pageNumber + 4
                            所以后台必须给我们传递过来这个值，最终想到，关于页面数据，全都封装在 pageBean中，于是要再加两个属性
                            startNumber 和  endNumber
                        --%>
                        <c:forEach var="num" begin="${pageBean.startNumber}" end="${pageBean.endNumber}" step="1">
                            <c:if test="${pageBean.pageNumber==num}">
                                <li class="curPage"><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=${num}&pageSize=5&cid=${cid}">${num}</a></li>
                            </c:if>
                            <c:if test="${pageBean.pageNumber!=num}">
                                <li><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=${num}&pageSize=5&cid=${cid}">${num}</a></li>
                            </c:if>
                        </c:forEach>


                        <li class="threeword"><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=${pageBean.pageNumber+1}&pageSize=5&cid=${cid}">${num}>下一页</a></li>
                        <li class="threeword"><a href="${ctx}/RouteServlet?action=findAllByCid&pageNumber=${pageBean.totalPage}&pageSize=5&cid=${cid}">末页</a></li>
                    </ul>
                </div>
            </div>
            <%--线路列表--%>
            <%--热门推荐 start--%>
            <div class="right">

                <div class="top">
                    <div class="hot">HOT</div>
                    <span>热门推荐</span>
                </div>
                <ul>
                    <li>
                        <div class="left"><img src="images/04-search_09.jpg" alt=""></div>
                        <div class="right">
                            <p>清远新银盏温泉度假村酒店/自由行套...</p>
                            <p>网付价<span>&yen;<span>899</span>起</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="left"><img src="images/04-search_09.jpg" alt=""></div>
                        <div class="right">
                            <p>清远新银盏温泉度假村酒店/自由行套...</p>
                            <p>网付价<span>&yen;<span>899</span>起</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="left"><img src="images/04-search_09.jpg" alt=""></div>
                        <div class="right">
                            <p>清远新银盏温泉度假村酒店/自由行套...</p>
                            <p>网付价<span>&yen;<span>899</span>起</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="left"><img src="images/04-search_09.jpg" alt=""></div>
                        <div class="right">
                            <p>清远新银盏温泉度假村酒店/自由行套...</p>
                            <p>网付价<span>&yen;<span>899</span>起</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="left"><img src="images/04-search_09.jpg" alt=""></div>
                        <div class="right">
                            <p>清远新银盏温泉度假村酒店/自由行套...</p>
                            <p>网付价<span>&yen;<span>899</span>起</span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
            <%--热门推荐 end--%>
        </div>
    </div>
</div>

<!--引入头部-->
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
