$(function(){
	/**
	 * 初始化列表
	 */
	var dict_treegrid=easyExt.treegrid({url:easyExt.url+'/sys_dictionary/findAll',treeField:'label',toolbar:'#dict_toolbar'});
	dict_treegrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'label',title:'标签',align:'left',width:100},
		 {field:'value',title:'值',align:'center',width:100},
		 {field:'type',title:'类型',align:'center',width:100},
		 {field:'status',title:'状态',align:'center',width:100,sortable:true,formatter:easyExt.formatterStatus},
		 {field:'remark',title:'备注',align:'center',width:120}
	]];
	dict_treegrid.loadFilter=function(r,parent){
		r.rows=	easyExt.convertTreegrid(r.rows,{
    		textField:'label'
    	});
		return r;
	}
	$('#dict_treegrid').treegrid(dict_treegrid);
	
	/**
	 * 初始化dialog
	 */
	var dict_dialog=easyExt.dialog({
		title:'部门修改',
		buttons:[{text:'确定',handler:function(){
			easyExt.form('#dictForm','/sys_dictionary/saveORupdate',function(){
				$('#dict_dialog').dialog('close');
				$('#dict_treegrid').treegrid('clearSelections');
				$('#dict_treegrid').treegrid('reload');
			});
		}},{text:'取消',handler:function(){
			$('#dict_dialog').dialog('close');
		}}]
	});
    $('#dict_dialog').dialog(dict_dialog);

});
$(function(){
	//删除实现
	$("#del_dict").click(function(){
		easyExt.delTreegrid({url:'/sys_dictionary/deleteBatch',treegrid_id:'#dict_treegrid'},function(){
			$('#dict_treegrid').treegrid('clearSelections');
			$('#dict_treegrid').treegrid('reload');
		});
	});
	//添加实现
	$("#add_dict").click(function(){
		$('#dictForm').form('clear');
		$('#dict_dialog').dialog('open');
	});
	//修改实现
	$("#edit_dict").click(function(){
		$('#dictForm').form('reset');
		var selRows=$('#dict_treegrid').treegrid('getChecked');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else if(selRows.length==1){
			$('#dictForm').form('load',selRows[0]);
			$('#dict_dialog').dialog('open');
		}else{
			$.messager.alert("提示", "只能选择其中一行进行修改!", "info");
		}
	});
	$('#parentId').combotree({
	    url: easyExt.url+'/sys_dictionary/findAll',
	    lines:true,
	    width:200,
	    loadFilter:function(r,parent){
	    	return easyExt.convert(r.rows,{
	    		textField:'label',
	    		level:1
	    	});
	    }
	});
	$('#status').combobox({
	    url:easyExt.url+'/sys_dictionary/findAll?type=sys_status',
	    valueField:'value',
	    textField:'label',
	    required:true,
	    width:200,
	    loadFilter:function(data){
	    	return data.rows;
	    }
	});
});