$(function() {
	easyExt.initUrl('/sys_log');
	easyExt.init();//初始化表格
});
function del(){
	var selRows=null;
	try{
		selRows=$('#dg').datagrid('getSelections');//返回选中行
	}catch(e){
		selRows=$('#tg').treegrid('getSelections');//返回选中行
	}
	if(selRows.length==0){
		$.messager.alert("提示", "请选择要删除的行！", "info");  
		return;
	}else{
		var ids="";  
        //批量获取选中行的ID  
        for (i = 0; i < selRows.length;i++) {  
            if (ids =="") {  
            	ids = selRows[i].id;  
            } else {  
            	ids = selRows[i].id + "," + ids;  
            }                 
        }  
        $.messager.confirm('提示', '是否删除选中数据?', function (r) {  
            if (!r) {  
                return;  
            }  
            easyExt.ajax(easyExt.url+"/deleteBatch","GET",{ids:ids});
			$('#dg').datagrid('reload'); 
			$('#dg').datagrid('clearSelections');  
        });
	}
};
