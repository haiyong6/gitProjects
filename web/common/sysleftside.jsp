<%@ page language="java" errorPage="/error.jsp"  pageEncoding="utf-8"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3Fa95f39fd3969500d36b8c556abd716e2' type='text/javascript'%3E%3C/script%3E"));
</script>
<!-- 左侧导航栏 -->
<aside class="aside" id="aside">
    <nav>
        <ul>
        	<sec:authorize ifAnyGranted="BDIP_PC_SYS_CONFIG">
            <li>
                <a class="title">系统管理</a>
                <ul class="list">
                    <li><a href="">系统登录日志</a></li>
                </ul>
            </li>
            </sec:authorize>
        </ul>
    </nav>
</aside>


<!-- 内容缩放栏 -->
<div class="btn-zoom-main" id="btn-zoom-main"><span></span></div>