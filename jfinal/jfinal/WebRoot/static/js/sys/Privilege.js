var privilege=privilege||{};
$(function() {
	easyExt.initUrl('/sys_privilege');
	easyExt.init();
	//权限列表
	$('#dg').datagrid({
		singleSelect: true,
		onBeforeLoad: function(row,param) {
			 if(!row){
				 param.type='O';
			 }
	     }
	});
	//菜单列表
	$('#tg').treegrid({
		 url:easyExt.url+'/findAll',
		 method: 'get',
	     fit : true,
		 fitColumns : true,
		 border : false,
		 idField : 'id',
		 treeField:'name',
		 iconCls: 'iconCls',
		 striped: true,
		 singleSelect: true,
		 autoRowHeight: false,
		 rownumbers: true,
		 showRefresh: true,
		 animate:true,
		 onClickRow: function(row){
			 $('#dg').datagrid('load',{parentId:row.id});
		 },
		 onBeforeLoad: function(row,param) {
			 if(!row){
				 param.type='F';
			 }
	     }
	 });
});
easyExt.addExt=function(){
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
};
privilege.addPermission=function(){
	var selRows=$('#tg').treegrid('getSelections');//返回选中行
	easyExt.ajax(easyExt.url+"/addPermission?pId="+selRows[0].id,"GET",{});
	$('#tg').treegrid('reload');
};
