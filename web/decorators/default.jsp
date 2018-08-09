<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
    <title><decorator:title/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
    <!--[if gt IE 8]>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <![endif]-->
    <link rel="icon" type="image/ico" href="../favicon.ico"/>
    <link href="${ctx}/css/stylesheets.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/js/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />
     
    <!--[if lt IE 8]>
        <link href="css/ie7.css" rel="stylesheet" type="text/css" />
    <![endif]-->            
    <link rel='stylesheet' type='text/css' href='${ctx}/js/ways/datepicker/ways-calendar.css' />
    
    <link href="${ctx}/js/plugins/showLoading/showLoading.css" rel="stylesheet" media="screen" /> 
    <link href='${ctx}/js/plugins/dropdown/dropdownCss.css' rel='stylesheet' type='text/css'  />
    
    <script type='text/javascript' src='${ctx}/js/plugins/jquery/jquery-1.10.2.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/jquery/jquery-migrate-1.2.1.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/jquery/jquery.mousewheel.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/cookie/jquery.cookies.2.2.0.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/bootstrap.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/sparklines/jquery.sparkline.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/select2/select2.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/select2/select2_locale_zh-CN.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/animatedprogressbar/animated_progressbar.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/dataTables/jquery.dataTables.min.js'></script>    
    <script type='text/javascript' src='${ctx}/js/plugins/fancybox/jquery.fancybox.pack.js'></script>
    <script type='text/javascript' src='${ctx}/js/cookies.js'></script>
    <script type='text/javascript' src='${ctx}/js/actions.js'></script>
    <script type='text/javascript' src='${ctx}/js/charts.js'></script>
    <script type='text/javascript' src='${ctx}/js/settings.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js'></script>
    <script type='text/javascript' src='${ctx}/js/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js'></script>
    
    <script type="text/javascript" src="${ctx}/js/app/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/app/appGlobal.js"></script>
    
    <script type="text/javascript" src="${ctx }/js/plugins/echarts/esl.js"></script>
    <script type="text/javascript" src="${ctx }/js/plugins/echarts/echarts.js"></script>
    
    <script type='text/javascript' src='${ctx}/js/plugins/showLoading/jquery.showLoading.min.js'></script>

    <script type="text/javascript">
		var ctx = "${ctx}";
    </script>
	<style>
	.wBlock .dSpace.fix{
		width:98%;
	}
	
	.breadcrumb {
	  padding: 2px 15px;
	  background-color: transparent;
	  -webkit-border-radius: 4px;
	     -moz-border-radius: 4px;
	          border-radius: 4px;
	}
	.breadcrumb li{
		display:inline;
	}
	
	/**
	*底部版权样式
	*/
	.page-footer {
		position: relative;
		z-index: 3;
		width: 100%;
		border-top: 1px solid #DCDCDC;
		text-align: center;
		line-height: 34px;
		color: #656565;
		margin-top: -20px;
	}
	
	/**
	*菜单栏选中状态样式
	*/
	.menu-select {
		background-color: #d0dae6 !important;
	}
	.menu-select .text{color:#3675b4 !important; font-weight: bold; font-family: '宋体';font-size: 13px !important;}
 	</style>
    <decorator:head/>        
</head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
<div class="wrapper"> 
	<div class="header">
	    <a class="logo" href="javascript:;"><img src="${ctx}/img/logo.png" alt="Aquarius -  responsive admin panel" title="Aquarius -  responsive admin panel"/></a>
	    <ul class="header_menu">
	        <li class="list_icon"><a href="index.html#">&nbsp;</a></li>
	        <li class="settings_icon">
	            <a href="wholesales.html#" class="link_themeSettings">&nbsp;</a>
	            <div id="themeSettings" class="popup">
	                <div class="head clearfix">
	                    <div class="arrow"></div>
	                    <span class="isw-settings"></span>
	                    <span class="name">皮肤设置</span>
	                </div>
	                <div class="body settings">
	                    <div class="row-fluid">
	                        <div class="span3"><strong>风格:</strong></div>
	                        <div class="span9">
	                            <a class="styleExample tip active" title="Default style" data-style="">&nbsp;</a>                                    
	                            <a class="styleExample silver tip" title="Silver style" data-style="silver">&nbsp;</a>
	                            <a class="styleExample dark tip" title="Dark style" data-style="dark">&nbsp;</a>
	                            <a class="styleExample marble tip" title="Marble style" data-style="marble">&nbsp;</a>
	                            <a class="styleExample red tip" title="Red style" data-style="red">&nbsp;</a>                                    
	                            <a class="styleExample green tip" title="Green style" data-style="green">&nbsp;</a>
	                            <a class="styleExample lime tip" title="Lime style" data-style="lime">&nbsp;</a>
	                            <a class="styleExample purple tip" title="Purple style" data-style="purple">&nbsp;</a>                                    
	                        </div>
	                    </div>                            
	                    <div class="row-fluid">
	                        <div class="span3"><strong>背景:</strong></div>
	                        <div class="span9">
	                            <a class="bgExample tip active" title="Default" data-style="">&nbsp;</a>
	                            <a class="bgExample bgCube tip" title="Cubes" data-style="cube">&nbsp;</a>
	                            <a class="bgExample bghLine tip" title="Horizontal line" data-style="hline">&nbsp;</a>
	                            <a class="bgExample bgvLine tip" title="Vertical line" data-style="vline">&nbsp;</a>
	                            <a class="bgExample bgDots tip" title="Dots" data-style="dots">&nbsp;</a>
	                            <a class="bgExample bgCrosshatch tip" title="Crosshatch" data-style="crosshatch">&nbsp;</a>
	                            <a class="bgExample bgbCrosshatch tip" title="Big crosshatch" data-style="bcrosshatch">&nbsp;</a>
	                            <a class="bgExample bgGrid tip" title="Grid" data-style="grid">&nbsp;</a>
	                        </div>
	                    </div>                            
	                    <div class="row-fluid">
	                        <div class="span3"><strong>是否隐藏菜单:</strong></div>
	                        <div class="span9">
	                            <input type="checkbox" name="settings_menu" value="1"/>
	                        </div>                                           
	                    </div>                            
	                </div>
	                <div class="footer">                            
	                    <button class="btn link_themeSettings" type="button">关闭</button>
	                </div>
	            </div>                    
	            
	        </li>
	    </ul>    
	</div><!-- header -->

    <div class="menu">                
        <div class="breadLine">            
            <div class="arrow"></div>
            <div class="adminControl active">
                您好, ${currUserDetails.userName}
            </div>
        </div><!-- breadLine -->
        <div class1="admin" style="display: none;">
            <div class="image">
                <img src="${ctx}/img/users/aqvatarius.jpg" class="img-polaroid"/>                
            </div>
            <ul class="control">                
                <li><span class="icon-comment"></span> <a href1="messages.html">消息</a> <a href="messages.html" class="caption red">0</a></li>
                <li><span class="icon-cog"></span> <a href1="forms.html">设置</a></li>
                <li><span class="icon-share-alt"></span> <a href1="login.html">退出</a></li>
            </ul>
            <div class="info">
                <span>所属企业：${currUserDetails.corpUnitName}</span>
            </div>
        </div><!-- admin -->
        <%@ include file="/common/leftside.jsp"%>
    </div><!-- mentu -->
    
    <div class="content">
		<decorator:body/>
    </div><!-- content -->
	<div class="footer page-footer">
		广州威尔森信息科技有限公司 
		<a href="http://www.miibeian.gov.cn" target="_blank">粤ICP备09206296号</a>
	</div>

	</div><!-- wrapper -->
</body>
</html>