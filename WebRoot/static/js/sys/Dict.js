$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/sys_dict/findAllByPage');
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/sys_dict/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_dict/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/sys_dict/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
});