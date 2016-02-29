$(function() {
	easyExt.initUrl('/sys_department');
	easyExt.initTreeGrid();
	
});
easyExt.addExt=function(){
	$('#parent').combotree({
		method: 'get',
	    url: easyExt.url+'/tree'
	});
};