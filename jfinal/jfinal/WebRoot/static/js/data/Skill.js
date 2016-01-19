var skill = skill || {};

$(function() {
	easyExt.initUrl('/data_skill');
	easyExt.init();// 初始化表格
});
skill.formater = function(value, row, index) {
	var s = "<a href='#' style='text-decoration: none;' onclick='skill.setPost("+ '"' + row.id + '"' + ")'>岗位设置 </a>";
	return s;
};
skill.setPost = function(id) {
	$('#dg').datagrid('clearSelections');
	skill.id=id;
	$('#setPost').dialog({
		closed : true,
		title : '设置岗位'
	});
	$('#setPost').dialog('open');
	$('#tt').tree({
		url : $u.getRootPath()+'/data_post/trees?id='+id,
		checkbox : true,
		method : 'get',
		lines : true
	});
};
skill.savePost=function(){
	var nodes = $('#tt').tree('getChecked', ['checked']);
	var $postIds="";
	var $gradeId=$('#grade').combobox('getValue');
	for ( var  i= 0; i < nodes.length-1; i++) {
		if(nodes[i].id!=0){
			$postIds+=nodes[i].id+",";
		}
	}
	if(nodes.length>0){
		$postIds+=nodes[nodes.length-1].id;
	}
	var $data={skillId:skill.id,postIds:$postIds,gradeId:$gradeId};
	$.ajax({
	       url: easyExt.url+'/savePost',
	       type: 'post',
	       dataType: 'json',
	       timeout: 8000,
	       data: $data
		}).done(function(data, status, xhr){   
			if(data.resCode=='1'){
				$.messager.alert("提示", "操作成功！", "info");
				$('#setPost').dialog('close');
			}else{
				$.messager.alert("提示", "操作失败！", "info");    
			}
		}).fail(function(data, status, xhr){
			$.messager.alert("提示", "请求失败！", "info");
		});
};
