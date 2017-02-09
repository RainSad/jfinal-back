$(function(){
	/**
	 * 初始化列表
	 */
	var dept_treegrid=easyExt.treegrid({url:easyExt.url+'/sys_dept/findAll',treeField:'deptName',toolbar:'#dept_toolbar'});
	dept_treegrid.columns=[[
		 {field:'id',hidden:true},
		 {field:'deptName',title:'部门名称',align:'left',width:100},
		 {field:'deptAddress',title:'部门地址',align:'center',width:120},
		 {field:'deptPhone',title:'部门电话',align:'center',width:100},
		 {field:'type',title:'类型',align:'center',width:80,formatter:function(value,row,index){
			 if(1==value)return "公司";else if(2==value)return "公司领导";else if(3==value)return "部门";else return "其它";;
		 }},
		 {field:'createTime',title:'时间',align:'center',width:120,sortable:true},
		 {field:'remark',title:'备注',align:'center',width:120}
	]];
	dept_treegrid.loadFilter=function(r,parent){
		r.rows=	easyExt.convertTreegrid(r.rows,{
    		textField:'deptName'
    	});
		return r;
	}
	$('#dept_treegrid').treegrid(dept_treegrid);
	
	/**
	 * 初始化dialog
	 */
	var dept_dialog=easyExt.dialog({
		title:'部门修改',
		buttons:[{text:'确定',handler:function(){
			easyExt.form('#deptForm','/sys_dept/saveORupdate',function(){
				$('#dept_dialog').dialog('close');
				$('#dept_treegrid').treegrid('clearSelections');
				$('#dept_treegrid').treegrid('reload');
			});
		}},{text:'取消',handler:function(){
			$('#dept_dialog').dialog('close');
		}}]
	});
    $('#dept_dialog').dialog(dept_dialog);
});
$(function(){
	//删除实现
	$("#del_dept").click(function(){
		easyExt.delTreegrid({url:'/sys_dept/deleteBatch',treegrid_id:'#dept_treegrid'},function(){
			$('#dept_treegrid').treegrid('clearSelections');
			$('#dept_treegrid').treegrid('reload');
		});
	});
	//添加实现
	$("#add_dept").click(function(){
		$('#deptForm').form('clear');
		$('#dept_dialog').dialog('open');
	});
	//修改实现
	$("#edit_dept").click(function(){
		$('#deptForm').form('reset');
		var selRows=$('#dept_treegrid').treegrid('getSelections');
		if(selRows.length==0){
			$.messager.alert("提示", "请选择其中的一行！", "info");  
			return;
		}else if(selRows.length==1){
			$('#deptForm').form('load',selRows[0]);
			$('#dept_dialog').dialog('open');
		}else{
			$.messager.alert("提示", "只能选择其中一行进行修改!", "info");
		}
	});
	$('#type').combobox({
	    url:easyExt.url+'/sys_dictionary/findAll?type=sys_dept_type',
	    valueField:'value',
	    textField:'label',
	    required:true,
	    width:200,
	    loadFilter:function(data){
	    	return data.rows;
	    }
	});
	$('#parentId').combotree({
	    url: easyExt.url+'/sys_dept/findAll',
	    lines:true,
	    width:200,
	    loadFilter:function(r,parent){
	    	return easyExt.convert(r.rows,{
	    		parentField:'parentId',
	    		textField:'deptName'
	    	});
	    }
	});
});