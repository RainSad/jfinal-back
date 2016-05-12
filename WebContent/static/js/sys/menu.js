$(function() {
	//初始化表格
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	
	//删除实现
	$("#del").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.del(selRows,'/sys_privilege/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#tg').treegrid('clearSelections');
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
		easyExt.edit(selRows,'/sys_privilege/updateOne',function(){
			$('#tg').treegrid('clearSelections');
			$('#tg').treegrid('reload'); 
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

