$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/sys_log/findAllByPage');
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/sys_log/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//导出实现
	$("#excel").click(function(){
		easyExt.exportExcel('/sys_log/exportExcel?'+$('#tForm').serialize());
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
});