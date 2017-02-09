/**
 * 提供easyui工具类
 */
var easyExt=easyExt||{};
easyExt.url=$u.getRootPath();
easyExt.formatterStatus=function(value,row,index){
	if(value==1||value=='1'){
		return "<div style=\"background-color:#5cb85c;width:60%;color:white;margin:0 auto;\">正常</div>";
	}else{
		return "<div style=\"background-color:#d9534f;width:60%;color:white;margin:0 auto;\">禁用</div>";
	}
};

easyExt.dialog=function(options){
	var settings = {
			maximizable:true,
			closed:true,
			title : null,
			iconCls:null,
			buttons:[]
	};
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	return settings;
}
easyExt.datagrid=function(options){
	var settings = {
		fitColumns:true,
		autoRowHeight:false,
		fitColumns:true,
		striped:true,
		singleSelect:false,
		method:'get',
		pagination: true,
		pagePosition:'bottom',
		checkOnSelect:true,
		selectOnCheck:true,
		collapsible: true,
		queryParams:{},
		fit : true,
		remoteSort:false,
		multiSort:true,
	    onLoadSuccess:function(){
		 $(this).datagrid('tooltip');
	    }
	};
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	return settings;
}
easyExt.treegrid=function(options){
	var settings = {
		 method: 'get',
		 fitColumns : true,
		 idField:"id",
		 treeField:"name",
		 animate: true,
		 fit : true,
		 singleSelect: false,
		 rownumbers: true,
		 autoRowHeight: false,
		 showRefresh: true,
		 collapsible: true
	};
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	return settings;
};
easyExt.clearCheckeds=function(tree_id){
	var sels=$(tree_id).tree('getChecked');
	for(var i=0;i<sels.length;i++){
		$(tree_id).tree('uncheck',sels[i].target);
	}
};
easyExt.del=function(options,callback){
	var settings = {
			id:'id',//主键
			datagrid_id:null,
			url:null,
			selRows:[]
	};
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	if(settings.datagrid_id==null){
		$.messager.alert("提示", "datagrid_id不能为空!", "info"); 
		return;
	}
	var selRows=$(settings.datagrid_id).datagrid('getChecked');
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	if(selRows.length==0){
		$.messager.alert("提示", "请选择要删除的行!", "info");  
		return;
	}else{
		var ids=[];  
        //批量获取选中行的ID  
        for (i = 0; i < selRows.length;i++) {  
        	ids.push(selRows[i][settings.id]); 
        }  
        $.messager.confirm('提示', '是否删除选中数据,?', function (r) {  
            if (!r) {  
                return;  
            }  
            $.post(easyExt.url+settings.url,{ids:ids},function(data){
            	easyExt.doError(data,callback);
            });
        });
	}
};
easyExt.delTreegrid=function(options,callback){
	var settings = {
			id:'id',//主键
			treegrid_id:null,
			url:null,
			selRows:[]
	};
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	if(settings.treegrid_id==null){
		$.messager.alert("提示", "treegrid_id!", "info"); 
		return;
	}
	var selRows=$(settings.treegrid_id).treegrid('getSelections');
	for(var i in options){
		settings[i] = options[i] || settings[i];
	}
	if(selRows.length==0){
		$.messager.alert("提示", "请选择要删除的行!", "info");  
		return;
	}else{
		var ids=[];  
        //批量获取选中行的ID  
        for (i = 0; i < selRows.length;i++) {  
        	ids.push(selRows[i][settings.id]);                 
        }  
        $.messager.confirm('提示', '是否删除选中数据,?', function (r) {  
            if (!r) {  
                return;  
            }  
            $.post(easyExt.url+settings.url,{ids:ids},function(data){
            	easyExt.doError(data,callback);
            });
        });
	}
};
/**
 * url: 远程端请求地址
 * easyui--表单提交
 */
easyExt.form=function($form,$url,callback){
	$($form).form('submit', {
		url:easyExt.url+$url,
	    onSubmit: function(){    
	    	var isValid = $(this).form('validate');
			return isValid;	// 返回false终止表单提交
	    },
	    success: function(data){
	    	easyExt.doError(data,callback);
	    }
	});
};
/*
 * 统一异常处理
 */
easyExt.doError=function(data,callback){
	var json = JSON.parse(data); 
	var resCode = json.resCode||0;
	var resMsg = json.resMsg|| "请求失败";
	if(resCode==1){
	   $.messager.alert("提示", resMsg, "info"); 
	   if(callback && typeof(callback) === "function"){
		   //window.setTimeout(function(){ 
	         callback(data);
           //},0); 
	   }
	}else {
		$.messager.alert("提示", resMsg, "error"); 
	    if(callback && typeof(callback) === "function"){
		   //window.setTimeout(function(){ 
	         callback(data);
           //},0); 
	    }
	}
};
/**
 * 树形控件的优化
 */
easyExt.convert=function(rows,opt){
	var idField=opt.idField || 'id',
	textField=opt.textField || 'name',
	openIcon=opt.openIcon || 'icon-metro-file',
	closedIcon=opt.closedIcon || 'icon-metro-folder',
	lv=opt.level||88,
	parentField= opt.parentField||"parentId";
	var nodes = [];
	// get the top level nodes
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if (row[parentField]==null||row[parentField]==0){
			var n={
					id:row[idField],
					text:row[textField],
					iconCls: row.iconCls||closedIcon,
					attributes:{level:1}
			};
			nodes.push(n);
		}
	}
	var toDo = [];
	for(var i=0; i<nodes.length; i++){
		toDo.push(nodes[i]);
	}
	while(toDo.length){
		var node = toDo.shift();	// the parent node
		var isLeaf=false;
		// get the children nodes
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			if(node.attributes.level!=lv){
				if (row[parentField] == node[idField]){
					var child = {id:row[idField],text:row[textField],attributes:{level:node.attributes.level+1}};
					if(!row.iconCls){
						child.iconCls=openIcon;
					}else{
						child.iconCls=row.iconCls;
					}
					if (node.children){
						node.children.push(child);
					} else {
						node.children = [child];
					}
					toDo.push(child);
					node.state='closed';
					if(!node.iconCls){
						node.iconCls=closedIcon;
					}
					isLeaf=true;
				}
			}
		}
		if(!isLeaf){
			node.state='open';
			if(!node.iconCls){
				node.iconCls=openIcon;
			}
		}
	}
	return nodes;
};
easyExt.convertTreegrid=function(rows,opt){
	var idField=opt.idField || 'id',
	textField=opt.textField || 'name',
	openIcon=opt.openIcon || 'icon-metro-file',
	closedIcon=opt.closedIcon || 'icon-metro-folder',
	lv=opt.level||88,
	parentValue=opt.parentValue||null,
	parentField= opt.parentField||"parentId";
	var nodes = [];
	// get the top level nodes
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if (row[parentField]==null||row[parentField]==0||row[parentField]==parentValue){
			var n={};
			for(var tem in row){
				n[tem]=row[tem];
				if(tem===idField){
					n.id=row[tem];
				}
				if(tem===textField){
					n.text=row[tem];
				}
                if(!row.level){
                	n.level=1;
                }
			}
			nodes.push(n);
		}
	}
	var toDo = [];
	for(var i=0; i<nodes.length; i++){
		toDo.push(nodes[i]);
	}
	while(toDo.length){
		var node = toDo.shift();	// the parent node
		var isLeaf=false;
		// get the children nodes
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			if(node.level!=lv){
				if (row[parentField] == node[idField]){
					var child={};
					for(var tem in row){
						child[tem]=row[tem];
						if(tem===idField){
							child.id=row[tem];
						}
						if(tem===textField){
							child.text=row[tem];
						}
		                if(!row.level){
		                	child.level=1;
		                }
					}
					if (node.children){
						node.children.push(child);
					} else {
						node.children = [child];
					}
					toDo.push(child);
					node.state='closed';
					if(!node.iconCls){
						node.iconCls=closedIcon;
					}
					isLeaf=true;
				}
			}
		}
		if(!isLeaf){
			node.state='open';
			if(!node.iconCls){
				node.iconCls=openIcon;
			}
		}
	}
	return nodes;
};

