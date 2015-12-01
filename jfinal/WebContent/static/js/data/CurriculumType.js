$(function() {
	easyExt.initUrl('/data_currType');
	$('#tg').treegrid({
		 url: easyExt.url+'/list',
		 method: 'get',
		 fitColumns : true,
		 idField : 'id',
		 treeField:'currTypeName',
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
				 param.id=0;
			 }
	     }
	 });
	$('#tg').treegrid().treegrid('clientPaging');
	
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/getMenu',
	    animate:true
	}); 
});
