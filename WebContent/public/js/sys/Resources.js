$(function(){
	/**
	 * 初始化列表
	 */
	var resources_treegrid=easyExt.treegrid({url:easyExt.url+'/sys_resources/findAll',toolbar:'#resources_toolbar',singleSelect:true});
	resources_treegrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'name',title:'资源名称',align:'left',width:100},
		 {field:'url',title:'资源地址',align:'center',width:100},
		 {field:'perm_code',title:'权限编码',align:'center',width:100},
		 {field:'level',title:'级数',align:'center',width:80},
		 {field:'sort',title:'排序',align:'center',width:80},
		 {field:'type',title:'类型',align:'center',width:80,formatter:function(value,row,index){
			 if(1==value)return "菜单";else if(2==value)return "权限";
		 }},
		 {field:'create_time',title:'时间',align:'center',width:120,sortable:true},
		 {field:'remark',title:'备注',align:'center',width:120},
		 {field:'status',title:'状态',align:'center',width:80,sortable:true,formatter:easyExt.formatterStatus}
	]];
	resources_treegrid.loadFilter=function(r,parent){
		r.rows=easyExt.convertTreegrid(r.rows,{});
		return r;
	}
	$('#resources_treegrid').treegrid(resources_treegrid);
	/**
	 * 初始化dialog
	 */
	var resources_dialog=easyExt.dialog({
		title:'资源修改',
		buttons:[{text:'确定',handler:function(){
			var p=$('#parentId').combotree('tree').tree('getSelected');
			if(p==null){
				$('#level').val(1);
			}else{
				$('#level').val(p.attributes.level+1);
			}
			easyExt.form('#resourcesForm','/sys_resources/saveORupdate',function(){
				$('#resources_dialog').dialog('close');
				$('#resources_treegrid').treegrid('clearSelections');
				$('#resources_treegrid').treegrid('reload');
			});
		}},{text:'取消',handler:function(){
			$('#resources_dialog').dialog('close');
		}}]
	});
    $('#resources_dialog').dialog(resources_dialog);

	$("#addPermission").click(function(){
		var selRows=$('#resources_treegrid').treegrid('getSelections');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else{
			var level=selRows[0].level;
			if(level<2){
				$.messager.alert("提示", "只有二级菜单才能添加基本权限!", "info");
			}else{
				$.post(easyExt.url+"/sys_resources/saveBasePermission",{parentId:selRows[0].id,level:selRows[0].level+1},function(res){
					$('#resources_treegrid').treegrid('clearSelections');
					$('#resources_treegrid').treegrid('reload');
				});
			}
		}
	});
});
$(function(){
	//删除实现
	$("#del_resources").click(function(){
		easyExt.delTreegrid({url:'/sys_resources/deleteBatch',treegrid_id:'#resources_treegrid'},function(){
			$('#resources_treegrid').treegrid('clearSelections');
			$('#resources_treegrid').treegrid('reload');
		});
	});
	//添加实现
	$("#add_resources").click(function(){
		$('#resourcesForm').form('clear');
		$('#resources_dialog').dialog('open');
	});
	//修改实现
	$("#edit_resources").click(function(){
		$('#resourcesForm').form('reset');
		var selRows=$('#resources_treegrid').treegrid('getSelections');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else if(selRows.length==1){
			$('#resourcesForm').form('load',selRows[0]);
			$('#resources_dialog').dialog('open');
		}else{
			$.messager.alert("提示", "只能选择其中一行进行修改!", "info");
		}
	});
	$('#status').combobox({
	    url:easyExt.url+'/sys_dictionary/findAll?type=sys_status',
	    valueField:'value',
	    textField:'label',
	    width:200,
	    loadFilter:function(data){
	    	return data.rows;
	    }
	});
	$('#type').combobox({
	    url:easyExt.url+'/sys_dictionary/findAll?type=sys_resources_type',
	    valueField:'value',
	    textField:'label',
	    width:200,
	    loadFilter:function(data){
	    	return data.rows;
	    }
	});
	$('#parentId').combotree({
	    url: easyExt.url+'/sys_resources/findAll?level=2',
	    lines:true,
	    width:200,
	    loadFilter:function(r,parent){
	    	return easyExt.convert(r.rows,{
	    		textField:'name'
	    	});
	    }
	});
});