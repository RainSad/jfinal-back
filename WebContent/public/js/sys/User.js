$(function(){
	/**
	 * 初始化弹出框
	 */
	var userDialog=easyExt.dialog({
		title:'用户信息修改',
		buttons:[{text:'确定',handler:function(){
			easyExt.form('#userForm','/sys_user/saveORupdate',function(){
				$('#user_dialog').dialog('close');
				$('#user_datagrid').datagrid('clearChecked');
				$('#user_datagrid').datagrid('reload');
			});
		}},{text:'取消',handler:function(){
			$('#user_dialog').dialog('close');
		}}]
	});
    $('#user_dialog').dialog(userDialog);

	/**
	 * 初始化用户列表
	 */
	var user_datagrid=easyExt.datagrid({url:easyExt.url+'/sys_user/findAllByPage',toolbar:'#user_toolbar',idField:'id'});
	user_datagrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'ck',checkbox:true},
		 {field:'username',title:'用户名',align:'center',width:100},
		 {field:'realname',title:'真实姓名',align:'center',width:100},
		 {field:'nickname',title:'昵称',align:'center',width:100},
		 {field:'deptName',title:'部门',align:'center',width:100},
		 {field:'phone',title:'电话',align:'center',width:100},
		 {field:'email',title:'邮箱',align:'center',width:100},
		 {field:'birthday',title:'生日',align:'center',width:100,sortable:true},
		 {field:'geneder',title:'性别',align:'center',width:60,formatter:function(value,row,index){
			 if(2==value)
				 return "女";
			 else if(1==value)
				 return "男";
			 else
				 return "不详";
		 }},
		 {field:'regTime',title:'注册时间',align:'center',width:100,sortable:true},
		 {field:'status',title:'状态',align:'center',width:80,sortable:true,formatter:easyExt.formatterStatus},
		 {field:'remark',title:'备注',align:'center',width:100}
	]];
	$('#user_datagrid').datagrid(user_datagrid);
    /**
     * 初始化角色
     */
	var userRoleDialog=easyExt.dialog({
		title:'用户角色修改',
		buttons:[{text:'确定',handler:function(){
		   var userId=$('#user_datagrid').datagrid('getChecked')[0].id;
           var sels=$('#role_tree').tree('getChecked');
           var roleIds=[];
           for(var i=0;i<sels.length;i++){
        	   roleIds.push(sels[i].id);
           }
		   $.post(easyExt.url+'/sys_role/saveUserRole',{userId:userId,roleIds:roleIds},function(){
			   $.messager.alert("提示", "保存用户角色成功!", "info");
			   $('#ur_dialog').dialog('close');
		   });
		}},{text:'取消',handler:function(){
			$('#ur_dialog').dialog('close');
		}}]
	});
    $('#ur_dialog').dialog(userRoleDialog);
    
	//用户角色实现
	$("#user_role").click(function(){
		easyExt.clearCheckeds('#role_tree');
		var selRows=$('#user_datagrid').datagrid('getChecked');
		if(selRows.length!=1){
			$.messager.alert("提示", "请选择一个用户进行修改!", "info");  
			return false;
		}else{
			$.get(easyExt.url+'/sys_role/getUserRole',{userId:selRows[0].id},function(res){
				res=JSON.parse(res);
				for(var i=0;i<res.rows.length;i++){
					var node = $('#role_tree').tree('find', res.rows[i].id);
					$('#role_tree').tree('check', node.target);
				}
			});
		}
		$('#ur_dialog').dialog('open');
	});
});
$(function(){
	//删除实现
	$("#del_user").click(function(){
		easyExt.del({url:'/sys_user/deleteBatch',datagrid_id:'#user_datagrid'},function(){
			$('#user_datagrid').datagrid('clearChecked');
			$('#user_datagrid').datagrid('reload');
		});
	});
	//添加实现
	$("#add_user").click(function(){
		$('#userForm').form('clear');
		$('#user_dialog').dialog('open');
	});
	//修改实现
	$("#edit_user").click(function(){
		$('#userForm').form('reset');
		var selRows=$('#user_datagrid').datagrid('getChecked');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else if(selRows.length==1){
			$('#userForm').form('load',selRows[0]);
			$('#user_dialog').dialog('open');
		}else{
			$.messager.alert("提示", "只能选择其中一行进行修改!", "info");
		}
	});
	//搜索实现
	$("#search_user").click(function(){
		$('#user_dialog').datagrid('load',$('#userSearchForm').serializeJson());
	});
	$('#dept').combotree({
		method: 'get',
		lines:true,
	    url: easyExt.url+'/sys_dept/findAll',
	    loadFilter:function(r,parent){
	    	return easyExt.convert(r.rows,{
	    		textField:'deptName'
	    	});
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
	$('#role_tree').tree({
		lines:true,
		animate:true,
		checkbox:true,
	    url:easyExt.url+'/sys_role/findAll',
	    loadFilter:function(r,parent){
	    	return easyExt.convert(r.rows,{
	    		textField:'roleName',
	    		openIcon:'icon-hamburg-my-account',
	    		closedIcon:'icon-hamburg-my-account'
	    	})
	    }
	});
});