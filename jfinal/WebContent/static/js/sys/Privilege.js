var privilege=privilege||{};
$(function() {
	easyExt.initUrl('/sys_privilege');
	easyExt.init();
	$('#dg').datagrid({
		singleSelect: true
	});
	$('#tg').treegrid({
		 url:easyExt.url+'/menuList',
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
		 }
	 });
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
});
privilege.addPermission=function(){
	var selRows=$('#tg').treegrid('getSelections');//返回选中行
	easyExt.ajax(easyExt.url+"/addPermission?pId="+selRows[0].id,"GET",{});
	$('#tg').treegrid('reload');
};