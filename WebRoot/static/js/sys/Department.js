$(function() {
	//初始化表格
	easyExt.initTreeGrid('#tg','/sys_department/findAll'); 
	//删除实现
	$("#del").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.del(selRows,'/sys_department/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#tg').treegrid('clearSelections');
			$('#tg').treegrid('reload');  
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_department/addOne',function(){
			$('#tg').treegrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.edit(selRows,'/sys_department/updateOne',function(){
			$('#tg').treegrid('clearSelections');
			$('#tg').treegrid('reload'); 
		});
	});
	$('#parent').combotree({
		method: 'get',
	    url: easyExt.url+'/sys_department/tree'
	});
});
