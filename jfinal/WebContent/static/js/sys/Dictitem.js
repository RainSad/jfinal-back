$(function() {
	easyExt.initUrl('/sys_dictitem');
	$('#tg').treegrid({
		 url: easyExt.url+'/list',
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
				 param.id=0;
			 }
	     }
	 });
	$('#tg').treegrid().treegrid('clientPaging');
});