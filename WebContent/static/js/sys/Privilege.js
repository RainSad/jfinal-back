$(function() {
	//权限列表
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	easyExt.initDataGrid('#dg','/sys_privilege/findAllByPage');
	
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/sys_privilege/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('clearSelections');
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_privilege/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/sys_privilege/updateOne',function(){
			$('#dg').datagrid('clearSelections');
			$('#dg').datagrid('reload'); 
		});
	});
	//添加基本权限
	$("#addPermission").click(function(){
		var selRows=$('#tg').treegrid('getSelections');//返回选中行
		$.get(easyExt.url+"/sys_privilege/addPermission",{pId:selRows[0].id},function(data){
			$('#dg').datagrid('reload'); 
		});
	});
	//上级菜单
	$('#parent').combotree({
		method: 'GET',
	    url: easyExt.url+'/sys_privilege/tree',
	    onSelect:function(node){
	    	$('#parentId').attr("value",node.id);
	    }
	});
});
