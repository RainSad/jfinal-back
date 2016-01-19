var enterprise=enterprise||{};

$(function() {
	easyExt.initUrl('/data_enterprise');
	easyExt.init();//初始化表格
	 
	enterprise.add = UE.getEditor('container3');
});
enterprise.edit=function(){
	var selRows=null;
	try{
		selRows=$('#dg').datagrid('getSelections');//返回选中行
	}catch(e){
		selRows=$('#tg').treegrid('getSelections');//返回选中行
	}
	$('#addForm').form('clear');
	if(selRows.length==0){
		$.messager.alert("提示", "请选择要修改的行！", "info");  
		return;
	}else if(selRows.length==1){
		UE.getEditor('container3').setContent(selRows[0].entDes);
		$('#add').dialog({
			iconCls:'icon-edit',
			title:'修改信息',
		    buttons:[{
				text:'确认',
				handler:function(){
					easyExt.form(easyExt.url+'/update');
				}
		      },{
				text:'取消',
				handler:function(){
					$('#add').dialog('close');
			    }
	        }]
		});
		$('#add').dialog('open');
		$('#addForm').form('myLoad',selRows[0]);
	}else{
		$.messager.alert("提示", "只能单项修改,请选择一行！", "info");
	}
};
