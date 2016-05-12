$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/data_broadband/findAllByPage');
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/data_broadband/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('clearSelections'); 
			$('#dg').datagrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/data_broadband/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/data_broadband/updateOne',function(data){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('clearSelections'); 
			$('#dg').datagrid('reload'); 
			if(data.resCode=='1'&&data.rows[0].status=='3'){
				 $.get("/weixin/api/sendMsg?weixinCode="+data.rows[0].weixinCode+"&name="+data.rows[0].userName,function(res){
				 });	
			}
		});
	});
});
function formatterStatus(value,row,index){
	  if(row.status=='1'||row.status==1){
		  return "<font color='blue'>申请中</font>";
	  }else if(row.status=='2'||row.status==2){
		  return "<font color='red'>取消办理</font>";
	  }else{
		  return "<font color='green'>办理成功</font>";
	  }
	 return s;
}
