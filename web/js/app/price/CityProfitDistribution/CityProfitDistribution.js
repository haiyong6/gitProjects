$(document).ready(function(){ 
	//页面加载隐藏图表面板
	$(".tab-content").attr("style","display:none");
	
	$('#startDate-container.input-append.date').datetimepicker({
		format: 'yyyy-mm',
        language:  'zh-CN',        
        todayBtn:  0,
		autoclose: 1,				
		startView: 3,		
		maxView:3,
		minView:3,
		startDate:beginDate,
		endDate:endDate,
        showMeridian: 1
    });
    $('#endDate-container.input-append.date').datetimepicker({
		format: 'yyyy-mm',
        language:  'zh-CN',        
        todayBtn:  0,
		autoclose: 1,				
		startView: 3,		
		maxView:3,
		minView:3,
		startDate:beginDate,
		endDate:endDate,
        showMeridian: 1
    });
    
	//时间改变事件
    $('#startDate-container.input-append.date').on('changeDate',function(){
    	var beginDate = $("#startDate").val();
		if(!beginDate)
		{
			alert("请选择时间");
			return;
		}
    	checkPopBoxData();
    }); 
	
	$('#cityModal').on('show', function () {
		showLoading("cityModalBody");
		$('#cityModalBody').load(ctx+"/global/getCityModal",getParams(),function(){
			//设置默认选中项
			$('#cityModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$("#cityModalBody").find('.cityModalByCity').each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			
			//行全选
			$("#cityModal").find(".cityModalByArea").each(function(){
				var allRowCityArr = $(this).parent().parent().parent().find(".cityModalByCity");
				var allRowSelectedCityArr = $(this).parent().parent().parent().find(".cityModalByCity:checked");
				if(allRowCityArr.length == allRowSelectedCityArr.length){
					$(this).attr("checked",'true');//全部全选		
				}else{
					$(this).removeAttr("checked");//全部取消
				}	
			});	
			//全选
			var allCityArr = $("#cityModal").find(".cityModalByCity");
			var allSelectedCityArr = $("#cityModal").find(".cityModalByCity:checked");
			//如果是全国均价则全选城市
			if(0 == $("#cityModalResultContainer div :input").val())
			{
				if($("#myModalLabel :checkbox").attr("checked")) 
				{
					$(".cityModalContainer").find('.cityModalByAll').click();
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
				else
				{
					$(".cityModalContainer").find('.cityModalByAll').click();
				}
			}
			else if(allCityArr.length == allSelectedCityArr.length){
				$(this).parents('#cityModal').find('.cityModalByAll').attr("checked",'true');//全部全选		
			}
			else{
				$(this).parents('#cityModal').find('.cityModalByAll').removeAttr("checked");//全部取消
			}
		});
	});
	
	$('#subModelModal').on('show', function (e) {
		if(e.relatedTarget)  return; //修复bootstrap的modal引入tabpane时，触发事件问题。
		
		//加载子车型数据
		showLoading("subModelModalBody");
		$('#subModelModalBody').load(ctx+"/global/getSubmodelModal",getParams(),function(){
			//弹出框设置默认选中项结果集		
			var strHTML = '<ul class="inline" >';
			$('#subModelModalResultContainer input').each(function(){
				var subModelId = $(this).val();
				var subModelName = $(this).attr("subModelName");
				var pooAttributeId = $(this).attr("pooAttributeId");
				var letter = $(this).attr("letter");
				strHTML += '<li style="padding-top:4px;margin-bottom:2px;">';
			  		strHTML += '<div class="removeBtnByResult label label-info" subModelId="'+subModelId+'"  pooAttributeId="'+pooAttributeId+'" letter="'+letter+'" subModelName="'+subModelName+'" style="cursor:pointer" title="删除：'+subModelName+'">';
				  		strHTML += '<i class="icon-remove icon-white"></i>'+subModelName;
			  		strHTML += '</div>';
			 		strHTML += '</li>';
			 		
		 		$(".subModelModalContainer .subModelIdInput").each(function(){
					if($(this).val() == subModelId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			strHTML += '</ul>';
			$("#selectorResultContainerBySubModel").html(strHTML);
			
			//addResultContainerBySubModel();
			/*
			//产地属性默认选中设置
			var selectedPooAttributeIds = $('.selectedPooAttributeIds').val();
			if(selectedPooAttributeIds != ""){
				$('#subModelModalBody').find('.pooAttributeIdInput').each(function(){
					var arr = selectedPooAttributeIds.split(",");
					for(var i=0;i<arr.length;i++){
						if(arr[i] == $(this).val()){
							$(this).attr("checked",'true');//选中
						}
					}					
				});
			}
			*/
		});
	});
	
	$('#versionModal').on('show', function (e) {		
		//修复bootstrap的modal引入tabpane时，触发事件问题。 或者没有选择车型
		if(e.relatedTarget || 0 == $("#subModelModalResultContainer input[name='selectedSubModel']").length)  return; 
		//加载子车型下型号数据
		showLoading("versionModalBody");
		$('#versionModalBody').load(ctx+"/global/getVersionModalByCommon",getParams(),function(){
			//设置默认选中项
			$('#versionModalResultContainer input').each(function(){
				var selectedId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function(){
					if($(this).val() == selectedId){
						$(this).attr("checked",'true');//行全选
					}
				});
			});
			
			//设置全选效果---开始
			$(".versionModalContainer").find('.selectVersionAll').each(function(){
				var selectedCount = 0;
				var totalCount = 0;
				var subModelId = $(this).val();
				$(".versionModalContainer .versionIdInput").each(function(){
					if(	subModelId == $(this).attr("subModelId")){
						totalCount++;
						if($(this).attr("checked")){
							selectedCount++;
						}
					}
				});
				if(selectedCount == totalCount){
					$(this).attr("checked",'true');
				}else{
					$(this).removeAttr("checked");//取消选中
				}
			});
			//设置全选效果---结束
			
		});
	});
	
	/**
	 * 校验弹出框有效数据
	 */
	function checkPopBoxData()
	{
		var paramsObj = getParams();
		if(!paramsObj.vids && !paramsObj.mids) return;
		$('.queryConditionContainer').showLoading();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/checkPopBoxData",
			   data: paramsObj,
			   dataType:'json',
			   success: function(data){
				   if(data)
				   {
					  var modelObj = $("#subModelModalResultContainer ul li");
					  var vidObj = $("#versionModalResultContainer ul li");
					  for(var i = 0; i < data.length; i++)
					  {
						  var mid = data[i].subModelId;
						  var vids = data[i].versionList;
						  //遍历车型
						  if(modelObj)
						  {
							  $.each(modelObj,function(i,n){
								  if(mid == $(n).find("input").val()) $(n).remove();
							  });
						  }
						  //遍历型号
						  if(vidObj && vids)
						  {
							  for(var j = 0; j < vids.length; j++)
							  {
								  var vid = vids[j].versionId;
								  $.each(vidObj,function(i,n){
									  if(vid == $(n).find("input").val()) $(n).remove();
								  });
							  }
						  }
					  }
				   }
				   $('.queryConditionContainer').hideLoading();
			   },
			   error:function(){
				   $('.queryConditionContainer').hideLoading();
			   }
			});
	}
	
	/**
	 * 获取页面请求参数
	 */
	function getParams()
	{
		var beginDate = $("#startDate").val();
		var endDate = $("#startDate").val();
		var inputType = 2;//控件是单选还是复选
		var analysisContentType = $("#analysisContentType").val();//数据指标分析类型
		//城市ID
		var citys = $("#cityModalResultContainer input[name='selectedCity']").map(function(){return $(this).val();}).get().join(",");
		//如果是全国均价，则取弹出枉里的城市ID
		var beginDate_TPMix=beginDate.substr(0,4)-1;
		if(0 == citys){
			if(beginDate_TPMix>2013){
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,19,30,41,42,48,60,61,47";
			}else{
				citys = "8,17,28,3,18,7,12,15,31,2,5,11,23,1,14,16,20,32,4,13,21,22,29,47";
			}
		}
		
		var vids = $("#versionModalResultContainer input[name='selectedVersion']").map(function(){return $(this).val();}).get().join(",");
		var mids = $("#subModelModalResultContainer input[name='selectedSubModel']").map(function(){return $(this).val();}).get().join(",");
		
		var paramObj = {};
		paramObj.beginDate = beginDate;
		paramObj.endDate = endDate;
		paramObj.modelIds = mids;
		paramObj.inputType = inputType;
		paramObj.citys = citys;
		paramObj.vids = vids;
		paramObj.mids = mids;
		paramObj.analysisContentType = analysisContentType;
		paramObj.timeType = "2";
		
		getQueryConditionAndBrowser(paramObj);
		return paramObj;
	}
	
	/**
	 * 获取页头查询条件，以及浏览器
	 */
	function getQueryConditionAndBrowser(paramObj)
	{
		paramObj.browser = navigator.appVersion;
		var queryCondition = "";
		
		queryCondition += "时间 = " + $("#startDate").val();
		
		queryCondition += "\n城市 = " + $("#cityModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n车型 = " + $("#subModelModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		queryCondition += "\n型号 = " + $("#versionModalResultContainer li div").map(function(){return $.trim($(this).text());}).get().join(",");
		
		
		paramObj.queryCondition = queryCondition;
	}
	
	/**
	 * 展示图形标题
	 */
	function showChartTitleDiv()
	{
		var span1Text = "";
		var span2Text = "";
		var beginDate = $("#startDate").val();
		var endDate = $("#startDate").val();
		span2Text = "(" + beginDate.substr(5) + "/" + beginDate.substr(0,4)+ "-" +  endDate.substr(5) + "/" + endDate.substr(0,4) + ")";
		//标题展示名称
		var dataName = "";
		var dataNameEn = "";
		if("1" == $("#analysisContentType").val())
		{
			dataName = "利润";
			dataNameEn = "profit";
		}
		else if("2" == $("#analysisContentType").val()) 
		{
			dataName = "折扣";
			dataNameEn = "distount";
		}
		else 
		{
			dataName = "成交价";
			dataNameEn = "TP";
		}
		
		//如果是城市利润对比
		if("2" == $("#analysisDimensionType").val())
		{
			var versionName = $("#versionModalResultContainer ul li div").text();
			span1Text = "RSD " + $("#subModelModalResultContainer ul li div").text() + " " + versionName.substr(5) + dataName + "走势";
			
			span2Text = "Dealer "+dataNameEn+" trend of " + $("#versionEn").val() + " RSD " + span2Text;
		}
		else 
		{
			if(1 < $("#subModelModalResultContainer ul li div input").length) span1Text = dataName + "走势";
			else span1Text = $("#subModelModalResultContainer ul li div").text() + dataName + "走势";
			
			
			span2Text = "Dealer "+dataNameEn+" trend" + span2Text;
		}
		$("#chartTitleDiv span").eq(0).text(span1Text);
		$("#chartTitleDiv span").eq(1).text(span2Text);
	}
	
	/**
	 * 画地图
	 */
	function showMapChart(json1,json2,json3,versionName)
	{
		/*if(json.isArray){
		  for(var i = 0; i < json.length;i++)
		  {
	        	json[i].itemStyle = {normal:{color:color}};
		  }
		}*/
		var option = {
			    title : {
			        text: '',
			        x:500,
			        y:10
			    },
			    tooltip : {
			        trigger: 'item'
			    },
			    legend: {
			        orient: 'vertical',
			        x:'center',
			        symbol: 'circle', 
			        data:["利润＞0","利润 ≦0","利润+奖励≦0"]
			    },
			    series : [
			        {
			                  name: '全国',
			                  type: 'map',
			                  hoverable: false,
			                  borderWidth:3,    
					          borderColor: '#fff',
			                  mapType: 'china',  
			                  itemStyle:{normal:{ borderColor: '#000',
			                        borderWidth: 2,
			                        color: "#CCCCCC",
			                        label: {
			                            show: false
			                        }}},
			                  data:[]
			                 
			                 
			        },
			        {
			            name: '利润＞0' ,
			            type: 'map',
			            mapType: 'china',
			            borderWidth:3,    
			            borderColor: '#fff',
			            hoverable: false,
			            itemStyle:{normal:{ borderColor: '#000',
			                        borderWidth: 2,
			                        color:'#009C0E',
			                        label: {
			                            show: false
			                        }}},
			            data : [
			            ],
			            markPoint : {
			                symbolSize: 4,      
			              symbol: 'circle', 
			              itemStyle: {
			                    normal: { 
			                        color:'#009C0E',	
			                        label: {
			                            show: true,
                                       position:'top',
			                          textStyle:{color:'#000'},
			                          formatter:function(v1,v2,v3)
			
			                          {   
			                          	return v2;
			                          }
			                            
			                        }
			                    }
			                },
			                data : json1
			            }
			        }
			        ,{
			            name: '利润 ≦0' ,
			            type: 'map',
			            mapType: 'china',
			            borderWidth:3,    
			            borderColor: '#fff',
			            hoverable: false,
			            itemStyle:{normal:{ borderColor: '#000',
			                        borderWidth: 2,
			                        color:'#F9A700',
			                        label: {
			                            show: false
			                        }}},
			            data : [
			            ],
			            markPoint : {
			                symbolSize: 4,      
			              symbol: 'circle', 
			              itemStyle: {
			                    normal: { 
			                        color:'#F9A700',	
			                        label: {
			                            show: true,
                                       position:'top',
			                          textStyle:{color:'#000'},
			                          formatter:function(v1,v2,v3)
			
			                          {   
			                          	return v2;
			                          }
			                            
			                        }
			                    }
			                },
			                data : json2
			            }
			        }
			        ,{
			            name: '利润+奖励≦0' ,
			            type: 'map',
			            mapType: 'china',
			            borderWidth:3,    
			            borderColor: '#fff',
			            hoverable: false,
			            itemStyle:{normal:{ borderColor: '#000',
			        	            color:'#E63110',
			                        borderWidth: 2,
			                        label: {
			                            show: false
			                        }}},
			            data : [
			            ],
			            markPoint : {
			                symbolSize: 4,      
			              symbol: 'circle', 
			              itemStyle: {
			                    normal: { 
			                        color:'#E63110',
			                        label: {
			                            show: true,
                                       position:'top',
			                          textStyle:{color:'#000'},
			                          formatter:function(v1,v2,v3)
			
			                          {   
			                          	return v2;
			                          }
			                            
			                        }
			                    }
			                },
			                data : json3
			            },
			            geoCoord: {
			                "青岛":[120.33,36.07],
			                "拉萨":[91.11,29.97],
			                "上海":[121.48,31.22],
			                "厦门":[118.1,24.46],
			                "福州":[119.3,26.08],
			                "东莞":[113.75,23.04],
			                "南宁":[108.33,22.84],
			                "广州":[113.23,23.16],
			                "太原":[112.53,37.87],
			                "昆明":[102.73,25.04],
			                "深圳":[114.07,22.62],
			                "海口":[110.35,20.02],
			                "沈阳":[123.38,41.8],
			                "长春":[125.35,43.88],
			                "银川":[106.27,38.47],
			                "南昌":[115.89,28.68],
			                "三亚":[109.511909,18.252847],
			                "吉林":[126.57,43.87],
			                "西宁":[101.74,36.56],
			                "呼和浩特":[111.65,40.82],
			                "成都":[104.06,30.67],
			                "西安":[108.95,34.27],
			                "重庆":[106.54,29.59],
			                "南京":[118.78,32.04],
			                "贵阳":[106.71,26.57],
			                "北京":[116.46,39.92],
			                "徐州":[117.2,34.26],
			                "衡水":[115.72,37.72],
			                "包头":[110,40.58],
			                "绵阳":[104.73,31.48],
			                "乌鲁木齐":[87.68,43.77],
			                "杭州":[120.19,30.26],
			                "济南":[117,36.65],
			                "温州":[120.65,28.01],
			                "九江":[115.97,29.71],
			                "兰州":[103.73,36.03],
			                "天津":[117.2,39.13],
			                "郑州":[113.65,34.76],
			                "哈尔滨":[126.63,45.75],
			                "石家庄":[114.48,38.03],
			                "长沙":[113,28.21],
			                "合肥":[117.27,31.86],
			                "武汉":[114.31,30.52],
							'宁波':[121.5967,29.6466],
							'大连':[122.2229,39.4409], 

			            }
			        }
			    ]
			};
		   myChart.clear();
		   myChart.setOption(option);
 		   
	}
	
	
	/**
	 * tr
	 * td1  td2  td3
	 * tr
	 * 动态生成表格数据
	 */
	function getElementTr(td1 ,c1, f1 , td2 ,c2 ,f2 , td3 ,c3 , f3){
		//默认样式
		if (c1 == null) c1 = 'black';if (c2 == null) c2 = 'black';if (c3 == null) c3 = 'black';
		if (f1 == null) f1 = 'initial';  if (f2 == null) f2 = 'initial'; if (f3 == null) f3 = 'initial';
		
		return "<tr> <td style = 'width:80px; color:"+c1+"; font-weight:"+f1+"'>" + td1 + " </td> <td style = 'width:80px; color:"+c2+
		"; font-weight:"+f3+"'>" + td2 +" </td> <td style = 'width:80px; color:"+c3+"; font-weight:"+f3+"'>" + td3 + " </td> </tr>";
	}
	
	/**
	 * 利润<=0 时(奖励+利润>0为橙色，奖励+利润<0为红色) 则为黑色
	 */
	function setFontColor(num,num2){
		var color = null;
		if(num<=0){
			if(num2>=0){
				color = '#FF8C00';
			}else{
				color = '#E63110';
			}
		}
		return color;
	}
	/**
	 * 画地图表格
	 * getElementTr(text1,color1,font1...3)生成表格1，2，3列，null为默认样式
	 */
	function showMapGrid(grid,versionName)
	{
		$('#titleId').html("<h5 >"+versionName+' 利润城市分布'+"</h5>");
		 var html_left = '<table>';  //左边表格
		 var html_right = '<table>'; //右边表格
		 var title = getElementTr('',null,null,'利润',null,'bold','利润+奖励',null,'bold');
		
		 /**
		  * 计算所选城市按大区统计集合
		  */
		 var hb = new Array(0,0);
		 var hz = new Array(0,0);
		 var x = new Array(0,0);
		 var db = new Array(0,0);
		 var hd = new Array(0,0);
		 var hn = new Array(0,0);
		 
		 var hb_count = 0;
		 var hz_count = 0;
		 var x_count = 0;
		 var db_count = 0;
		 var hd_count = 0;
		 var hn_count = 0;
		 
		 /**
		  * 所选城市按大区计算显示在头部
		  */
		 for(var j = 0; j < grid.length; j++){
			 
			 if(grid[j].areaName == '华北区'){
				 if(grid[j].modelProfit != '-')
				 hb[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(hb[0]));
				 hb[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(hb[1]));
			 	 hb_count = hb_count + 1;
			 }else if(grid[j].areaName == '华中区'){
				 if(grid[j].modelProfit != '-')
				 hz[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(hz[0]));
				 hz[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(hz[1]));
			 	 hz_count = hz_count + 1;
			 }else if(grid[j].areaName == '西区'){
				 if(grid[j].modelProfit != '-')
				 x[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(x[0]));
				 x[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(x[1]));
			 	 x_count = x_count + 1;
			 }else if(grid[j].areaName == '东北区'){
				 if(grid[j].modelProfit != '-')
				 db[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(db[0]));
				 db[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(db[1]));
			 	 db_count = db_count + 1;
			 }else if(grid[j].areaName == '华东区'){
				 if(grid[j].modelProfit != '-')
				 hd[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(hd[0]));
				 hd[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(hd[1]));
			 	 hd_count = hd_count + 1;
			 }else if(grid[j].areaName == '华南区'){
				 if(grid[j].modelProfit != '-')
				 hn[0] = ( parseFloat(grid[j].modelProfit)+ parseFloat(hn[0]));
				 hn[1] = ( parseFloat((grid[j].modelProfit == '-')?0:grid[j].modelProfit)+ parseFloat(grid[j].rewardAssessment)+ parseFloat(hn[1]));
			 	 hn_count = hn_count + 1;
			 }
		 }
		 //利润取大区平均
		 hb[0] = hb[0]/hb_count;
		 hz[0] = hz[0]/hz_count;
		 x[0]  =  x[0]/x_count;
		 db[0] = db[0]/db_count;
		 hd[0] = hd[0]/hd_count;
		 hn[0] = hn[0]/hn_count;
		 //利润+奖励取大区平均
		 hb[1] = hb[1]/hb_count;
		 hz[1] = hz[1]/hz_count;
		 x[1]  =  x[1]/x_count;
		 db[1] = db[1]/db_count;
		 hd[1] = hd[1]/hd_count;
		 hn[1] = hn[1]/hn_count;
		 
		 var rowSize = 1;
		 var areas = new Array();//记录所选城市的大区
		 var backCity = 0;
		 for(var i = 0; i < grid.length; i++){
			 if(rowSize<22){//显示到左边，左边满了显示在右边
				 
			     if(areas.toString().indexOf(grid[i].areaId) == -1){//当前城市没有大区，就上面先画个大区
				     html_left += getElementTr('',null,null,'',null,null,'',null,null);//换行
				     html_left += getElementTr('',null,null,'',null,null,'',null,null);//换行
				     html_left += title;
				     
				     //画大区
				     if(grid[i].areaName=='华北区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(hb[0]),null,null,formatData(hb[1]),setFontColor(hb[0],hb[1]),null);
				     }else if(grid[i].areaName=='华中区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(hz[0]),null,null,formatData(hz[1]),setFontColor(hz[0],hz[1]),null);
				     }else if(grid[i].areaName=='西区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(x[0]),null,null,formatData(x[1]),setFontColor(x[0],x[1]),null);
				     }else if(grid[i].areaName=='东北区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(db[0]),null,null,formatData(db[1]),setFontColor(db[0],db[1]),null);
				     }else if(grid[i].areaName=='华东区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(hd[0]),null,null,formatData(hd[1]),setFontColor(hd[0],hd[1]),null);
				     }else if(grid[i].areaName=='华南区'){
				    	 html_left += getElementTr(grid[i].areaName,null,'bold',formatData(hn[0]),null,null,formatData(hn[1]),setFontColor(hn[0],hn[1]),null);
				     }
				     areas.push(grid[i].areaId);
				     rowSize+=2;
			     }
			     rowSize+=1;
			     //画城市
			     html_left += getElementTr(grid[i].cityName,null,null,formatData(grid[i].modelProfit),null,null,formatData(parseFloat(grid[i].modelProfit=='-'?0:grid[i].modelProfit)+parseFloat(grid[i].rewardAssessment)),setFontColor(parseFloat(grid[i].modelProfit),parseFloat(grid[i].modelProfit)+parseFloat(grid[i].rewardAssessment)),null);
			 
			 }else{  //显示到右边
				 if(areas.toString().indexOf(grid[i].areaId) == -1){//当前城市没有大区，就上面先画个大区
					 html_right += getElementTr('',null,null,'',null,null,'',null,null);//换行
					 html_right += getElementTr('',null,null,'',null,null,'',null,null);//换行
					 html_right += title;
					 
					 //画大区
					 if(grid[i].areaName=='华北区'){
						 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(hb[0]),null,null,formatData(hb[1]),setFontColor(hd[0],hd[1]),null);
				     }else if(grid[i].areaName=='华中区'){
				    	 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(hz[0]),null,null,formatData(hz[1]),setFontColor(hz[0],hz[1]),null);
				     }else if(grid[i].areaName=='西区'){ 
				    	 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(x[0]),null,null,formatData(x[1]),setFontColor(x[0],x[1]),null);
				     }else if(grid[i].areaName=='东北区'){
				    	 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(db[0]),null,null,formatData(db[1]),setFontColor(db[0],db[1]),null);
				     }else if(grid[i].areaName=='华东区'){
				    	 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(hd[0]),null,null,formatData(hd[1]),setFontColor(hd[0],hd[1]),null);
				     }else if(grid[i].areaName=='华南区'){
				    	 html_right += getElementTr(grid[i].areaName,null,'bold',formatData(hn[0]),null,null,formatData(hn[1]),setFontColor(hn[0],hn[1]),null);
				     }
					 areas.push(grid[i].areaId);
					 rowSize+=2;
				 }
				 rowSize+=1;
				 //画城市
				 html_right += getElementTr(grid[i].cityName,null,null,formatData(grid[i].modelProfit),null,null,formatData(parseFloat(grid[i].modelProfit=='-'?0:grid[i].modelProfit)+parseFloat(grid[i].rewardAssessment)),setFontColor(parseFloat(grid[i].modelProfit),parseFloat(grid[i].modelProfit)+parseFloat(grid[i].rewardAssessment)),null);
			 }
		 }
		 html_left += '</table>';
		 
		 html_right += '</table>';
		 
		 $('#id1').html( html_left );
		 $('#id2').html( html_right );
	}
	
	/**
	 * 画数据表格
	 */
	function showDataGrid(grid){
		var tbodyHtml = "";
		if(grid)
		{
			for(var i = 0; i < grid.length; i++)
			{
				var dataObj = grid[i];
				var className = "";
				if(0 == i % 2) className = "odd";
				tbodyHtml += "<tr>";
			/*	//如果是城市利润对比
				if("2" == $("#analysisDimensionType").val())
				{
					tbodyHtml += "<td style='width:5%' class='"+className+"'>" + dataObj.cityName + "</td>";
					$("#cityTh").removeClass("hide");
					//如果是城市利润时保存型号英文名称
					if(0 == i)$("#versionEn").val(dataObj.versionChartName);
				}
				else $("#cityTh").addClass("hide");
				*/
				tbodyHtml += "<td style='width:5%' class='"+className+"'>" + dataObj.yearMonth + "</td>";
				tbodyHtml += "<td style='width:10%' class='"+className+"'>" + dataObj.versionCode + "</td>";
				tbodyHtml += "<td style='width:15%' class='"+className+"'>" + dataObj.versionName +"</td>";
				tbodyHtml += "<td style='width:7%;text-align: right;' class='"+className+"'>" + dataObj.versionLaunchDate + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.msrp) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + dataObj.cityName + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.tp) + "</td>";
				tbodyHtml += "<td style='width:8%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.sellerCost) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.invoicePrice) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.rebatePrice) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.rewardAssessment) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.promotionalAllowance) + "</td>";
				tbodyHtml += "<td style='width:6%;text-align: right;' class='text-right "+className+"'>" + formatData(dataObj.modelProfit) + "</td>";
				tbodyHtml += "</tr>";
			}
			$("#gridTbody").html(tbodyHtml);
	}
	}
	
	/**
	 * 参数验证
	 */
	function paramsValidate()
	{
		var beginDate = $("#startDate").val();
		var endDate = $("#startDate").val();
		//城市ID
		var cityLength = $("#cityModalResultContainer input[name='selectedCity']").length;
		var vidLength = $("#versionModalResultContainer input[name='selectedVersion']").length;
		var modelLength = $("#subModelModalResultContainer :input[name='selectedSubModel']").length;
		
		if(!beginDate)
		{
			alert("请选择时间");
			return true;
		}
		
		if(0 == cityLength)
		{
			alert("请选择城市");
			return true;
		}
		if(0 == modelLength)
		{
			alert("请选择车型");
			return true;
		}
		if(0 == vidLength)
		{
			alert("请选择型号");
			return true;
		}
		
		return false;
	}
	
	$('#queryBtn').on('click', function (e) {
		if(paramsValidate()) return;
		
		$('body').showLoading();
		//默认展示图表
		$("#showChart").click();
		//发送请求
		$.ajax({
			   type: "POST",
			   url: ctx+"/loadModelProfitChartAndTableMap",
			   data: getParams(),
			   success: function(data){
				   $(".tab-content").attr("style","display:block;")//显示隐藏的图表面板
				   if(data)
				   {
				     //查询面板折叠
				     $(".queryConditionContainer .buttons .toggle a").click();
					 showMapChart(data.chart1,data.chart2,data.chart3,data.versionName);
					 showMapGrid(data.grid,data.versionName);
					 showDataGrid(data.grid);
				   }
				   $('body').hideLoading();
			   },
			   error:function(){
				   $('body').hideLoading();
			   }
			});
	});
	
	$('#resetBtn').on('click', function (e) {
		//城市容器
		$('#cityModalResultContainer').html("");
		//车型容器
		$('#subModelModalResultContainer').html("");
		//型号容器
		$('#versionModalResultContainer').html("");
		
		$('#formId :reset');	
	});
});

/**
 * 导出Excel
 * @param exportType 1:英文版;2:中文版
 */
function exportExcel(exportType)
{
	if(!$.trim($("#gridTbody").html()))
	{
		alert("暂无数据导出");
		return;
	}
	$("#languageType").val(exportType);
	$("#exportFormId").submit();
}



/**
 * 车型弹出框
 * @param type
 */
function showSubModel(type)
{
	if(type==1){
		$("#choseType").attr("value","1")//本竞品
	}else if(type==2){
		$("#choseType").attr("value","2")//细分市场
	}else if(type==3){
		$("#choseType").attr("value","3")//品牌
	}else if(type==4){
		$("#choseType").attr("value","4")//厂商
	}
	
	/*根据选择条件隐藏*/
	var pooAttributeIdArr = [];
	$(".subModelModalContainer").find('.pooAttributeIdInput').each(function(){
		
		if($(this).attr("checked")){
			
			pooAttributeIdArr[pooAttributeIdArr.length] = $(this).val();
		}
	});	
	
	$(".subModelModalContainer").find(".subModelIdInput").each(function(){
		var flag = false;
		for(var i=0;i<pooAttributeIdArr.length;i++){
			if(pooAttributeIdArr[i] == $(this).attr("pooAttributeId")){
				flag = true;
				break;
			}
		}
		if(flag){
			$(this).parent().show();
		}else{
			$(this).parent().hide();
		}
	});
	/*根据选择条件隐藏结束*/
	
	var inputType = "2";//默认弹出框为单选
	
	if('1' == type) getModelPage("tabs-competingProducts",type,inputType,"1");
	else if('2' == type) getModelPage("tabs-segment",type,inputType,"1");
	else if('3' == type) getModelPage("tabs-brand",type,inputType,"1");
	else getModelPage("tabs-manf",type,inputType,"1");
};
