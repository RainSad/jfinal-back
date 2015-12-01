var qOption=qOption||{};

$(function() {
	easyExt.initUrl('/data_qOption');
	easyExt.init();//初始化表格
});

qOption.formater=function(value,row,index){
	  if(row.isRight==1){
	    return '正确';
	  }else{
	    return '错误';
	  }
};
