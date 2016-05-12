$(function() {
	//初始化表格
	easyExt.initDataGrid('#dg','/data_broadband/findAllByPageResidence');
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
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
