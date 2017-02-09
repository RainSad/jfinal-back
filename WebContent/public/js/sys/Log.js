$(function(){
	/**
	 * 初始化列表
	 */
	var log_datagrid=easyExt.datagrid({url:easyExt.url+'/sys_log/findAllByPage',toolbar:'#log_toolbar',idField:'id'});
	log_datagrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'ck',checkbox:true},
		 {field:'ip',title:'ip地址',align:'center',width:100},
		 {field:'os',title:'操作系统',align:'center',width:100},
		 {field:'browser',title:'浏览器',align:'center',width:100},
		 {field:'optUser',title:'用户名',align:'center',width:100},
		 {field:'realname',title:'姓名',align:'center',width:100},
		 {field:'reqUrl',title:'请求地址',align:'center',width:100},
		 {field:'reqParam',title:'请求参数',align:'center',width:100},
		 {field:'isSuccess',title:'是否成功',align:'center',width:60,sortable:true,formatter:function(value,row,index){
			 if(1==value)
				return "<div style=\"background-color:#5cb85c;width:60%;color:white;margin:0 auto;\">成功</div>";
			 else
				return "<div style=\"background-color:#d9534f;width:60%;color:white;margin:0 auto;\">失败</div>";
		 }},
		 {field:'createTime',title:'时间',align:'center',width:100,sortable:true},
		 {field:'remark',title:'备注',align:'center',width:100}
	]];
	$('#log_datagrid').datagrid(log_datagrid);
	
	//删除实现
	$("#del_log").click(function(){
		easyExt.del({url:'/sys_log/deleteBatch',datagrid_id:'#log_datagrid'},function(){
			$('#log_datagrid').datagrid('clearChecked');
			$('#log_datagrid').datagrid('reload');
		});
	});
	//导出实现
	$("#log_excel").click(function(){
		easyExt.exportExcel('/sys_log/exportExcel?'+$('#logSearchForm').serialize());
	});
	//搜索实现
	$("#search_log").click(function(){
		$('#log_datagrid').datagrid('load',$('#logSearchForm').serializeJson());
	});
});