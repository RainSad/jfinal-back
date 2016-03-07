$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/sys_user/findAllByPage');
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/sys_user/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_user/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/sys_user/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	$('#dept').combotree({
		method: 'get',
	    url: easyExt.url+'/sys_department/tree'
	});
	//用户角色实现
	$("#user_role").click(function(){
		var selRows=$('#dg').datagrid('getSelections');//返回选中行
		if(selRows.length==0){
			$.messager.alert("提示", "请选择要修改的用户角色！", "info");  
			return;
		}else if(selRows.length==1){
			getUserRole(selRows[0]);
		}else{
			$.messager.alert("提示", "只能单项修改,请选择一行！", "info");
		}
	});
	function getUserRole(user){
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
					    easyExt.ajax({url:easyExt.url+"/sys_role/saveUserRole?userId="+user.id+"&roleId="+roleId},function(data,status, xhr){
					    	$.messager.alert("提示", "请求成功！", "info");
					    });
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
		easyExt.initDataGrid('#ur_dg','/sys_role/findAllByPage');
		$('#ur_dg').datagrid({
			onLoadSuccess: function() {
				$('#ur_dg').datagrid('tooltip');
				easyExt.ajax({url:easyExt.url+"/sys_role/getUserRole",type:'get',data:{userId:user.id}},function(data,status, xhr){
					if(data){
						for(var i=0,j=data.length;i<j;i++){
							$('#ur_dg').datagrid('selectRecord',data[i].columns.id);
						}
					} 
				});
		     }
		});
		$('#ur_dg').datagrid('clearSelections'); //清除选择项
	}
});