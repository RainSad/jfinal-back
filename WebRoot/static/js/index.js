$(function(){
	/**********填充时间*************/
	$u.clockon();
	setInterval($u.clockon,1000);
	
	/**********填充主题*************/
	$('#themeSelector').combobox({
		valueField: 'id',
		textField: 'text',
		data:[
		  {id:"metro-blue",text:"磁贴蓝(默认)",selected:true},
		  {id:"metro-standard",text:"磁贴(标准)"},
		  {id:"metro-cyan",text:"磁贴(青)"},
		  {id:"metro-purple",text:"磁贴(紫)"},
		  {id:"metro-light-red",text:"磁贴(浅红)"},
		  {id:"metro-light-green",text:"磁贴(浅绿)"},
		  {id:"metro-gray",text:"磁贴(灰)"},
		  {id:"metro-green",text:"磁贴(绿)"},
		  {id:"metro-orange",text:"磁贴(橙)"},
		  {id:"metro-red",text:"磁贴(红)"},
		  {id:"metro-yellow",text:"磁贴(黄)"},
		  {id:"default",text:"天空蓝(推荐)"},
		  {id:"bootstrap",text:"银色(推荐)"},
		  {id:"gray",text:"灰霾(推荐)"},
		  {id:"ui-cupertino",text:"清泉"},
		  {id:"ui-pepper-grinder",text:"杏黄"}
		],
		onSelect: function(record){
				changeTheme(record.id);
		}
	});
	function changeTheme(themeName){
		var t=$('#theme');
		var href=t.attr("href");
		href=href.replace(/\/easyui.css/g,'');
		var pos=href.lastIndexOf("/");
		href=href.substring(0,pos+1);
		t.attr("href",href+themeName+"/easyui.css");
		t.attr("type","text/css");
		t.attr("rel","stylesheet");
	    var $iframe = $('iframe');
	    if ($iframe.length > 0) {
	        for ( var i = 0; i < $iframe.length; i++) {
	           var ifr = $iframe[i];
	           $(ifr).contents().find('#theme').attr('href', href+themeName+"/easyui.css");
	        }
	    }
	    $.cookie('theme', themeName, {
	    	expires : 30,
	    	path: '/'
	    });
	}
	/**********折叠北部*************/
	$("#btnHideNorth").click(
		function () { 
			$(main).layout("collapse", "north");
		}
	);
	/**********退出系统*************/
	$("#btnExit").click(
		function(){
			if (window.confirm("确认要退出购优惠管理平台？")) {
				window.location.href = 'exit';
			}
		}
	);
	/**********个人信息*************/
	$("#btnUser").click(
		function(){
			$.messager.alert("提示", "个人信息！", "info");
		}
	);
	/**********跳转至主页*************/
	$("#mainTabs_jumpHome").click(
		function(){
			$('#mainTabs').tabs('select',0);
		}
	);
	var type="collapse";
	/**********最大/小化*************/
	$("#mainTabs_toggleAll").click(
		function(){
			$(main).layout(type,"north");
			$(main).layout(type,"south");
			$(main).layout(type,"west");
			$(main).layout(type,"east");
			if(type=="collapse"){
				type="expand";
			}else{
				type="collapse";
			}
		}
	);
	/**********刷新tab*************/
	$("#mainTabs_refTab").click(
		function(){
			var tab = $('#mainTabs').tabs('getSelected');  
			$('#mainTabs').tabs('update', {
				tab: tab,
				options: {
					title: tab.panel('options').title,
					content: tab.panel('options').content  
				}
			});
		}
	);
	/**********关闭全部tab*************/
	$("#mainTabs_closeTab").click(
		function(){
			var allTabs = $("#mainTabs").tabs('tabs');
		    for(var i = 0, len = allTabs.length; i < len; i++) {
		      $("#mainTabs").tabs('close', 1);
		    }
		}
	);
	/**********全屏切换*************/
	$("#btnFullScreen").click(
	   function(){
		   if (fullScreenApi.supportsFullScreen) {
			   if(!fullScreenApi.isFullScreen()){
				   fullScreenApi.requestFullScreen(document.documentElement);
			   }else{
				   fullScreenApi.cancelFullScreen(document.documentElement);
			   }
			}else{
				alert("您的破浏览器不支持全屏API哦，请换高版本的chrome或者firebox！");
			}
	   }
	);
});
/**********添加tab*************/
function addTab(title, url) {
	if ($('#mainTabs').tabs('exists', title)) {
		$('#mainTabs').tabs('select', title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0"  src="' + url
				+ '" style="width:100%;height:100%"></iframe>';
		$('#mainTabs').tabs('add', {
			title : title,
			content : content,
			closable : true
		});
	}
};