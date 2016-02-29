$(function() {
	easyExt.initUrl('/sys_privilege');
	easyExt.initTreeGrid();//菜单列表
	
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
});