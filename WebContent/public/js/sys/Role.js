$(function(){
	/**
	 * 初始化列表
	 */
	var role_datagrid=easyExt.datagrid({url:easyExt.url+'/sys_role/findAllByPage',toolbar:'#role_toolbar'});
	role_datagrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'ck',checkbox:true},
		 {field:'roleName',title:'角色名称',align:'center',width:120},
		 {field:'roleCode',title:'角色编码',align:'center',width:120},
		 {field:'createTime',title:'时间',align:'center',width:120,sortable:true},
		 {field:'remark',title:'备注',align:'center',width:120},
		 {field:'status',title:'状态',align:'center',width:80,sortable:true,formatter:easyExt.formatterStatus}
	]];
	$('#role_datagrid').datagrid(role_datagrid);
	
	/**
	 * 初始化dialog
	 */
	var role_dialog=easyExt.dialog({
		title:'角色修改',
		buttons:[{text:'确定',handler:function(){
			easyExt.form('#roleForm','/sys_role/saveORupdate',function(){
				$('#role_dialog').dialog('close');
				$('#role_datagrid').datagrid('clearChecked');
				$('#role_datagrid').datagrid('reload');
			});
		}},{text:'取消',handler:function(){
			$('#role_dialog').dialog('close');
		}}]
	});
    $('#role_dialog').dialog(role_dialog);
	
	/**
     * 初始化资源
     */
	var roleResourcesDialog=easyExt.dialog({
		title:'角色权限修改',
		buttons:[{text:'确定',handler:function(){
		   var roleId=$('#role_datagrid').datagrid('getChecked')[0].id;
           var sels=$('#resources_tree').tree('getChecked');
           var resourcesIds=[];
           for(var i=0;i<sels.length;i++){
        	   resourcesIds.push(sels[i].id);
           }
		   $.post(easyExt.url+'/sys_resources/saveRoleResources',{roleId:roleId,resourcesIds:resourcesIds},function(){
			   $.messager.alert("提示", "保存角色权限成功!", "info");
			   $('#rr_dialog').dialog('close');
		   });
		}},{text:'取消',handler:function(){
			$('#rr_dialog').dialog('close');
		}}]
	});
	$('#rr_dialog').dialog(roleResourcesDialog);

	$('#role_resource').click(function(){
		easyExt.clearCheckeds('#resources_tree');
		var selRows=$('#role_datagrid').datagrid('getChecked');
		if(selRows.length!=1){
			$.messager.alert("提示", "请选择一个角色进行修改!", "info");  
			return false;
		}else{
			$.get(easyExt.url+'/sys_resources/getRoleResources',{roleId:selRows[0].id},function(res){
				res=JSON.parse(res);
				for(var i=0;i<res.rows.length;i++){
					var node = $('#resources_tree').tree('find', res.rows[i].id);
					var children=$('#resources_tree').tree('getChildren',node.target);
					$('#resources_tree').tree('check', node.target);
				}
			});
		}
		$('#rr_dialog').dialog('open');
	});
});
$(function(){
	//删除实现
	$("#del_role").click(function(){
		easyExt.del({url:'/sys_role/deleteBatch',datagrid_id:'#role_datagrid'},function(){
			$('#role_datagrid').datagrid('clearChecked');
			$('#role_datagrid').datagrid('reload');
		});
	});
	//添加实现
	$("#add_role").click(function(){
		$('#roleForm').form('clear');
		$('#role_dialog').dialog('open');
	});
	//修改实现
	$("#edit_role").click(function(){
		$('#roleForm').form('reset');
		var selRows=$('#role_datagrid').datagrid('getChecked');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else if(selRows.length==1){
			$('#roleForm').form('load',selRows[0]);
			$('#role_dialog').dialog('open');
		}else{
			$.messager.alert("提示", "只能选择其中一行进行修改!", "info");
		}
	});

	$('#status').combobox({
	    url:easyExt.url+'/sys_dictionary/findAll?type=sys_status',
	    valueField:'value',
	    textField:'label',
	    width:200,
	    required:true,
	    loadFilter:function(data){
	    	return data.rows;
	    }
	});
	$('#resources_tree').tree({
		lines:true,
		cascadeCheck:false,
		animate:true,
		checkbox:true,
	    url:easyExt.url+'/sys_resources/findAll',
	    loadFilter:function(r,parent){
	    	var r=easyExt.convertTreegrid(r.rows,{
	    		parentField:'parentId',
	    		textField:'name'
	    	})
	    	return r;
	    }
	});
});

