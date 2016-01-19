$(function() {
	easyExt.initUrl('/sys_privilege');
	//菜单列表
	$('#tg').treegrid({
		 url: easyExt.url+'/findAll',
		 method: 'get',
		 fitColumns : true,
		 idField : 'id',
		 treeField:'name',
		 fit : true,
		 rownumbers: true,
		 toolbar: '#toolbar',
		 pageSize: 10,
		 autoRowHeight: false,
		 showRefresh: true,
		 pagination: true,
		 animate: true,
		 collapsible: true,
		 pagePosition: 'bottom',
		 onBeforeLoad: function(row,param) {
			 if(!row){
				 param.type='F';
			 }
	     }
	 }).treegrid('clientPaging');
	
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
