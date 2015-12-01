var easyExt=easyExt||{};
easyExt.url=$u.getRootPath();
easyExt.initUrl=function(url){
	easyExt.url+=url;
};
easyExt.initTreeGrid=function(){
	$('#tg').treegrid({
		 url: easyExt.url+'/menuList',
		 method: 'get',
	     fit : true,
		 fitColumns : true,
		 border : false,
		 idField : 'id',
		 treeField:'name',
		 iconCls: 'iconCls',
		 rownumbers: true,
		 toolbar: '#toolbar',
		 singleSelect: false,
		 autoRowHeight: false,
		 striped: true,
		 collapsible: true,
		 showRefresh: true,
		 nowrap: true,
		 animate:true
	 });
};
easyExt.init=function(){
	$('#dg').datagrid({
		 url: easyExt.url+'/list',
		 striped: true,
		 fit : true,
		 method: 'get',
		 autoRowHeight:false,
		 pagination: true,
		 toolbar: '#toolbar',
		 showRefresh: true,
		 pagePosition: 'bottom',
		 nowrap: true,
		 collapsible: true,
		 idField: 'id',
		 fitColumns: true,
		 rownumbers: true,
		 onLoadError: function(){
			 $.messager.alert("提示", "数据加载失败！", "info");
		 },
		 onLoadSuccess: function(){
			 $('#dg').datagrid('tooltip');
		 }
	 });
};
easyExt.exportExcel=function(){
	var url = easyExt.url+"/exportExcel?"+$('#tForm').serialize();
	window.location.href = url;
};
easyExt.del=function(){
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
            easyExt.ajax(easyExt.url+"/updateBatch","GET",{ids:ids});
			try{
				$('#dg').datagrid('reload'); 
				$('#dg').datagrid('clearSelections');  
			}catch(e){
				$('#tg').treegrid('reload');
				$('#tg').treegrid('clearSelections');
			}
        });
	}
};
easyExt.add=function(){
	$('#addForm').form('clear');
	$('#add').dialog({
		iconCls:'icon-save',
		title:'添加信息',  
		buttons:[{
				text:'确认',
				handler:function(){
					easyExt.form(easyExt.url+'/save');
				}
			},{
				text:'取消',
				handler:function(){
					$('#add').dialog('close');
			}
	    }]
	});
	$('#add').dialog('open');
};
easyExt.edit=function(){
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
easyExt.form=function($url){
	$('#addForm').form('submit', {
		url:$url,
	    onSubmit: function(){    
	    	var isValid = $(this).form('validate');
			return isValid;	// 返回false终止表单提交
	    },
	    success: function(data){
			var data = JSON.parse(data); 
			if (data.resCode==1){
				$.messager.alert("提示", "操作成功！", "info"); 
				$('#add').dialog('close');
				try{
					$('#dg').datagrid('reload'); 
					$('#tg').treegrid('reload');
				}catch(e){
				}
			}else{
				$.messager.alert("提示", "操作失败！", "info"); 
			}
	    }
	});
};

easyExt.viewDialog=function(index){
	$('#dg').datagrid('clearSelections'); //清除选择项
	var json=$('#dg').datagrid('getData').rows[index];
	$('#view').dialog({closed: true,title:'查看信息'});
	$('#view').dialog('open');
	$('#viewForm').form('load',json);
	$("#viewForm input").prop("readonly", true);
};

easyExt.updateStatus=function(id,status){
	easyExt.ajax(easyExt.url+"/update","POST",{status:status,id:id});
	$('#dg').datagrid('reload'); 
};

easyExt.formaterOperate=function(value,row,index){
	 var s="<a href='#' style='text-decoration: none;' onclick='easyExt.viewDialog("+'"'+index+'"'+")'>查看 | </a>";
	  if(row.status=='1'){
	    s+="<a href='#' style='text-decoration: none;' onclick='easyExt.updateStatus("+'"'+row.id+'","'+"0"+'"'+")'>关闭</a>";
	    return s;
	  }else{
	    s+="<a href='#' style='text-decoration: none;' onclick='easyExt.updateStatus("+'"'+row.id+'","'+"1"+'"'+")'>开启</a>";
	    return s;
	  }
	 return s;
};

easyExt.initPassword=function(id){
	var pwd='******';
	easyExt.ajax(easyExt.url+"/update","POST",{password:pwd,id:id});
};
easyExt.searchs=function(){
	   var o = {};  
	    $('#dg').datagrid('load',$('#tForm').serializeJson());
};
easyExt.ajax=function($url,$type,$data){
	var $datas={};
    $.ajax({
    	url: $url,
    	type: $type,
    	data: $data,
    	dataType: 'json',
    	contentType:'application/json;charset=utf-8',
    	timeout: 5000
    	}).done(function(data, status, xhr){  
    		$datas=data;
    		if(data.resCode=='1'){
    			$.messager.alert("提示", "操作成功！", "info");
    			$('#dg').datagrid('reload'); 
    		}else{
    			$.messager.alert("提示", "操作失败！", "info");
    		}
    	}).fail(function(data, status, xhr){
    		$.messager.alert("提示", "请求失败！", "info");
    		return;
    });
    return $datas;
};
