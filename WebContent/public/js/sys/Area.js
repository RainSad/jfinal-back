$(function(){
	/**
	 * 初始化列表
	 */
	var area_treegrid2=easyExt.treegrid({url:easyExt.url+'/sys_area/findAll',treeField:'areaName'});
	area_treegrid2.columns=[[
		 {field:'id',hidden:true},
		 {field:'areaName',title:'区域名称',align:'left',width:120},
		 {field:'areaCode',title:'区域编码',align:'center',width:100},
		 {field:'shortName',title:'区域简称',align:'center',width:100},
		 {field:'lng',title:'经度',align:'center',width:100},
		 {field:'lat',title:'纬度',align:'center',width:100},
		 {field:'sort',title:'排序',align:'center',width:80},
		 {field:'level',title:'级数',align:'center',width:80},
		 {field:'status',title:'状态',align:'center',width:100,sortable:true,formatter:easyExt.formatterStatus}
	]];
	area_treegrid2.loadFilter=function(r,parent){
		var sels=$('#area_treegrid1').treegrid('getSelections')[0];
		var parentValue=null;
		if(sels){
			parentValue=sels.id;
		}
		r.rows=	easyExt.convertTreegrid(r.rows,{
    		textField:'areaName',
    		parentValue:parentValue
    	});
		return r;
	}
	$('#area_treegrid2').treegrid(area_treegrid2);
	
	var area_treegrid1=easyExt.treegrid({url:easyExt.url+'/sys_area/findAll?parentId=-1',singleSelect:true,treeField:'areaName'});
	area_treegrid1.columns=[[
		 {field:'id',hidden:true},
		 {field:'areaName',title:'区域名称',align:'left',width:100},
		 {field:'areaCode',title:'区域编码',align:'center',width:80}
	]];
	area_treegrid1.loadFilter=function(r,parent){
		r.rows=	easyExt.convertTreegrid(r.rows,{
    		textField:'areaName',
    	});
		return r;
	}
	area_treegrid1.onClickRow=function(rowData){
		$('#area_treegrid2').treegrid('reload',{parentId:rowData.id});
	}
	$('#area_treegrid1').treegrid(area_treegrid1);
});