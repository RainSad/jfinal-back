 $(function(){
	$.get(easyExt.url+"/sys_user/getUserPermission",function(res){
		res=JSON.parse(res);
		for(var i=0;i<res.rows.length;i++){
			var menu={};
			if(res.rows[i].columns.level==1){
				menu.title=res.rows[i].columns.name;
				menu.selected=false;
				menu.iconCls=res.rows[i].columns.iconCls;
				menu.content="";
				for(var j=0;j<res.rows.length;j++){
					if(res.rows[j].columns.level!=-1&&res.rows[j].columns.parent_id==res.rows[i].columns.id){
						menu.content+="<a class=\"easyui-linkbutton\" data-options=\"plain:true,iconCls:'"+res.rows[j].columns.iconCls+"'\" style=\"margin-bottom:5px;\" onclick=\"addTab('"+res.rows[j].columns.name+"','"+easyExt.url+res.rows[j].columns.url+"')\">"+res.rows[j].columns.name+"</a>";
					}
				}
				$('#myMenu').accordion('add',menu);
			}
		}
		$('#myMenu').accordion('add', {
			title: '',
			content: '',
			selected: true
		});
		$('#username').html(res.extra.columns.username);
		$('#realname').html(res.extra.columns.realname+",您好!");
	});
	var themes={
	  "metro-blue": "#daeef5",
	  "metro-purple":"#dfd7f6",
	  "metro-light-red":"#e8c5e8",
	  "metro-light-green":"#d7eecf",
	  "metro-gray":"#c7ccd1",
	  "metro-green":"#e5f0c9",
	  "metro-orange":"#f0e3bf",
	  "metro-red":"#f0e1e3",
	  "default":"#E0ECFF",
	  "bootstrap":"#F2F2F2",
	  "gray":"#f3f3f3",
	  "ui-cupertino":"#d7ebf9"	
	}
	if($.cookie('theme')){
		$('#themeSelector').combobox('setValue', $.cookie('theme'));
		$('.panel-common').css('background-color',themes[$.cookie('theme')]);
	}
	/**********填充主题*************/
	$('#themeSelector').combobox({
		valueField: 'id',
		textField: 'text',
		data:[
			  {id:"metro-blue",text:"磁贴蓝(默认)",selected:true},
			  {id:"metro-purple",text:"磁贴(紫)"},
			  {id:"metro-light-red",text:"磁贴(浅红)"},
			  {id:"metro-light-green",text:"磁贴(浅绿)"},
			  {id:"metro-gray",text:"磁贴(灰)"},
			  {id:"metro-green",text:"磁贴(绿)"},
			  {id:"metro-orange",text:"磁贴(橙)"},
			  {id:"metro-red",text:"磁贴(红)"},
			  {id:"default",text:"天空蓝(推荐)"},
			  {id:"bootstrap",text:"银色(推荐)"},
			  {id:"gray",text:"灰霾(推荐)"},
			  {id:"ui-cupertino",text:"清泉"}
		],
		onSelect: function(record){
			$('.panel-common').css('background-color',themes[record.id]);
			changeTheme(record.id);
		}
	});
	function changeTheme(themeName){
		var t=$('#theme');
		var href=$u.getRootPath()+"/public/plugins/easyui/themes/"+themeName+"/easyui.css";
		t.attr("href",href);
	    var $iframe = $('iframe');
	    if ($iframe.length > 0) {
	        for ( var i = 0; i < $iframe.length; i++) {
	           var ifr = $iframe[i];
	           $(ifr).contents().find('#theme').attr('href',href);
	        }
	    }
	    $.cookie('theme', themeName, {
	    	expires : 30,
	    	path: '/'
	    });
	    
	}

	/**********退出系统*************/
	$("#btnExit").click(
		function(){
			if (window.confirm("确认要退出后台管理系统平台？")) {
				window.location.href = easyExt.url+'/logout';
			}
		}
	);
	/**********修改密码*************/
	$("#btnUserPwd").click(
		function(){
			var pasDialog=easyExt.dialog({
				title:'密码修改',
				iconCls:'icon-dortmund-lock',
				buttons:[{text:'密码修改',handler:function(){
					easyExt.form('#user_update_password_form','/sys_user/updatePassword',function(){
						$('#user_update_password_dialog').dialog('close');
					});
				}},{text:'取消',handler:function(){
					$('#user_update_password_dialog').dialog('close');
				}}]
			});
            $('#user_update_password_dialog').dialog(pasDialog).dialog('open');
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