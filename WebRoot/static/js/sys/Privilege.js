$(function() {
	//权限列表
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	easyExt.initDataGrid('#dg','/sys_privilege/findAllByPage');
	
	//删除实现
	$("#del").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.del(selRows,'/sys_privilege/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#tg').treegrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_privilege/addOne',function(){
			$('#tg').treegrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.edit(selRows,'/sys_privilege/updateOne');
	});
	//添加基本权限
	$("#addPermission").click(function(){
		var selRows=$('#tg').treegrid('getSelections');//返回选中行
		easyExt.ajax({url:easyExt.url+"/sys_privilege/addPermission?pId="+selRows[0].id,type:'GET'},function(data, status, xhr){
			if(data){
				$.messager.alert("提示", "添加基本权限成功！", "info");
			}
		});
	});
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/sys_privilege/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
});
