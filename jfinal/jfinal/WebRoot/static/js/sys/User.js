var user=user||{};
$(function() {
	easyExt.initUrl('/sys_user');
	easyExt.init();//初始化表格
	
});
easyExt.addExt=function(){
	$('#dept').combotree({
		method: 'get',
	    url: $u.getRootPath()+'/sys_department/tree'
	});
};
user.viewRole=function(){
	var selRows=$('#dg').datagrid('getSelections');//返回选中行
	if(selRows.length==0){
		$.messager.alert("提示", "请选择要修改的用户角色！", "info");  
		return;
	}else if(selRows.length==1){
		user.getUserRole(selRows[0]);
	}else{
		$.messager.alert("提示", "只能单项修改,请选择一行！", "info");
	}
};

user.getUserRole=function(user){
	var json={};
	var roleId="";
	$('#userRole').dialog({
		title:'用户角色管理',  
		buttons:[{
				text:'确认',
				handler:function(){
					var role=$('#ur_dg').datagrid('getSelections');
					for(var i=0,j=role.length;i<j;i++){
						roleId+=role[i].id+",";
					}
					easyExt.ajax($u.getRootPath()+"/sys_role/saveUserRole?userId="+user.id+"&roleId="+roleId,"POST",{});
					$('#userRole').dialog('close');
				}
			},{
				text:'取消',
				handler:function(){
					$('#userRole').dialog('close');
			}
	    }]
	});
	$('#userRole').dialog('open');
	$('#ur_dg').datagrid({
		 url: $u.getRootPath()+'/sys_role/findAllByPage',
		 striped: true,
		 fit : true,
		 method: 'get',
		 autoRowHeight:false,
		 pagination: true,
		 showRefresh: true,
		 pagePosition: 'bottom',
		 idField: 'id',
		 fitColumns: true,
		 rownumbers: true,
		 onLoadSuccess:function(){
		    //获取用户拥有角色,选中
		    $.ajax({
				type:'get',
		    	dataType: 'json',
		    	timeout: 5000,
		    	data:{userId:user.id},
				url:$u.getRootPath()+'/sys_role/getUserRole'
		    }).done(function(data, status, xhr){
				if(data){
					for(var i=0,j=data.length;i<j;i++){
						$('#ur_dg').datagrid('selectRecord',data[i].columns.id);
					}
				} 
		    }).fail(function(data, status, xhr){
	    		$.messager.alert("提示", "请求失败！", "info");
	    		return;
		    });
		 }
	 });
	$('#ur_dg').datagrid('clearSelections'); //清除选择项
};