var role=role||{};
role.id="";
$(function() {
	easyExt.initUrl('/sys_role');
	easyExt.init();// 初始化表格
	$('#tg').treegrid({
		 url: $u.getRootPath()+'/sys_privilege/findAll',
		 method: 'get',
	     fit : true,
		 fitColumns : true,
		 border : false,
		 singleSelect: false,
		 idField : 'id',
		 treeField:'name',
		 iconCls: 'iconCls',
		 rownumbers: true,
		 toolbar: '#toolbar1',
		 animate:true,
		 onClickRow: function(row){
    	     //级联选择  
             $(this).treegrid('cascadeCheck',{  
                 id:row.id, //节点ID  
                 deepCascade:true //深度级联  
             }); 
		 }
	 });
});


function lookP(id){
	role.id=id;
	$('#dg').datagrid('clearSelections');
    $.ajax({
    	url:  easyExt.url+"/getPrivilege?id="+id,
    	type: "GET",
    	dataType: 'json'
    	}).done(function(data, status, xhr){  
    		$('#tg').treegrid("unselectAll");
			for(var i=0;j=data.length,i<j;i++){
				$('#tg').treegrid("select",data[i].columns.id);
			}
    });
}
role.formatter=function(value, row, index){
	return '<a href="javascript:lookP('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="查看权限"></div></a>';
};
role.savePermission=function(){
	var p=$('#tg').treegrid("getSelections");
	var pIds="";
	for(var i=0;j=p.length,i<j;i++){
		pIds+=p[i].id+",";
	}
	easyExt.ajax(easyExt.url+"/saveRolePrivilege?roleId="+role.id+"&privilegeIds="+pIds,"POST",{});
};