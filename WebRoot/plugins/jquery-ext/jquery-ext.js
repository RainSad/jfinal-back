/************序列化表单为json对象***************/
(function($){  
    $.fn.serializeJson=function(){  
        var serializeObj={};  
        var array=this.serializeArray();  
        var str=this.serialize();  
        $(array).each(function(){  
            if(serializeObj[this.name]){  
                if($.isArray(serializeObj[this.name])){  
                    serializeObj[this.name].push(this.value);  
                }else{  
                    serializeObj[this.name]=[serializeObj[this.name],this.value];  
                }  
            }else{  
                serializeObj[this.name]=this.value;   
            }  
        });  
        return serializeObj;  
    };  
})(jQuery); 
/******************easyui表单插件，使其支持二级对象***********************/
$.extend($.fn.form.methods, {
	myLoad : function (jq, param) {
		return jq.each(function () {
			load(this, param);
		});

		function load(target, param) {
			if (!$.data(target, "form")) {
				$.data(target, "form", {
					options : $.extend({}, $.fn.form.defaults)
				});
			}
			var options = $.data(target, "form").options;
			if (typeof param == "string") {
				var params = {};
				if (options.onBeforeLoad.call(target, params) == false) {
					return;
				}
				$.ajax({
					url : param,
					data : params,
					dataType : "json",
					success : function (rsp) {
						loadData(rsp);
					},
					error : function () {
						options.onLoadError.apply(target, arguments);
					}
				});
			} else {
				loadData(param);
			}
			function loadData(dd) {
				var form = $(target);
				var formFields = form.find("input[name],select[name],textarea[name]");
				formFields.each(function(){
					var name = this.name;
					var value = jQuery.proxy(function(){try{return eval('this.'+name);}catch(e){return "";}},dd)();
					var rr = setNormalVal(name,value);
					if (!rr.length) {
						var f = form.find("input[numberboxName=\"" + name + "\"]");
						if (f.length) {
							f.numberbox("setValue", value);
						} else {
							$("input[name=\"" + name + "\"]", form).val(value);
							$("textarea[name=\"" + name + "\"]", form).val(value);
							$("select[name=\"" + name + "\"]", form).val(value);
						}
					}
					setPlugsVal(name,value);
				});
				options.onLoadSuccess.call(target, dd);
				$(target).form("validate");
			};
			function setNormalVal(key, val) {
				var rr = $(target).find("input[name=\"" + key + "\"][type=radio], input[name=\"" + key + "\"][type=checkbox]");
				rr._propAttr("checked", false);
				rr.each(function () {
					var f = $(this);
					if (f.val() == String(val) || $.inArray(f.val(), val) >= 0) {
						f._propAttr("checked", true);
					}
				});
				return rr;
			};
			function setPlugsVal(key, val) {
				var form = $(target);
				var cc = ["combobox", "combotree", "combogrid", "datetimebox", "datebox", "combo"];
				var c = form.find("[comboName=\"" + key + "\"]");
				if (c.length) {
					for (var i = 0; i < cc.length; i++) {
						var combo = cc[i];
						if (c.hasClass(combo + "-f")) {
							if (c[combo]("options").multiple) {
								c[combo]("setValues", val);
							} else {
								c[combo]("setValue", val);
							}
							return;
						}
					}
				}
			};
		};
	}
});
/**
 * Datagrid扩展方法tooltip 基于Easyui 1.3.3，可用于Easyui1.3.3+
 * 使用 $('#dg').datagrid('tooltip');
 */
$.extend($.fn.datagrid.methods, {
	tooltip : function (jq, fields) {
		return jq.each(function () {
			var panel = $(this).datagrid('getPanel');
			if (fields && typeof fields == 'object' && fields.sort) {
				$.each(fields, function () {
					var field = this;
					bindEvent($('.datagrid-body td[field=' + field + '] .datagrid-cell', panel));
				});
			} else {
				bindEvent($(".datagrid-body .datagrid-cell", panel));
			}
		});

		function bindEvent(jqs) {
			jqs.mouseover(function () {
				var content = $(this).text();
				$(this).tooltip({
					content : content,
					trackMouse : true,
					onHide : function () {
						$(this).tooltip('destroy');
					}
				}).tooltip('show');
			});
		}
	}
});
/**
 * easyui表单序列化json
 * $('#form').serialize();
 */
$.extend($.fn.form.methods, {  
    serialize: function(jq){  
        var arrayValue = $(jq[0]).serializeArray();
		var json = {};
		$.each(arrayValue, function() {
			var item = this;
			if (json[item["name"]]) {
				json[item["name"]] = json[item["name"]] + "," + item["value"];
			} else {
				json[item["name"]] = item["value"];
			}
		});
		return json; 
    },
    getValue:function(jq,name){  
        var jsonValue = $(jq[0]).form("serialize");
		return jsonValue[name]; 
    },
    setValue:function(jq,name,value){
		return jq.each(function () {
				_b(this, _29);
				var data = {};
				data[name] = value;
				$(this).form("load",data);
		});
	}
});  
/************easyui时间插件***************/
(function ($) {
	$.fn.my97 = function (options, params) {
		if (typeof options == "string") {
			return $.fn.my97.methods[options](this, params);
		}
		options = options || {};
		if (!WdatePicker) {
			alert("未引入My97js包！");
			return;
		}
		return this.each(function () {
			var data = $.data(this, "my97");
			var newOptions;
			if (data) {
				newOptions = $.extend(data.options, options);
				data.opts = newOptions;
			} else {
				newOptions = $.extend({}, $.fn.my97.defaults, $.fn.my97.parseOptions(this), options);
				$.data(this, "my97", {
					options : newOptions
				});
			}
			$(this).addClass('Wdate').click(function () {
				WdatePicker(newOptions);
			});
		});
	};
	$.fn.my97.methods = {
		setValue : function (target, params) {
			target.val(params);
		},
		getValue : function (target) {
			return target.val();
		},
		clearValue : function (target) {
			target.val('');
		}
	};
	$.fn.my97.parseOptions = function (target) {
		return $.extend({}, $.parser.parseOptions(target, ["el", "vel", "weekMethod", "lang", "skin", "dateFmt", "realDateFmt", "realTimeFmt", "realFullFmt", "minDate", "maxDate", "startDate", {
						doubleCalendar : "boolean",
						enableKeyboard : "boolean",
						enableInputMask : "boolean",
						autoUpdateOnChanged : "boolean",
						firstDayOfWeek : "number",
						isShowWeek : "boolean",
						highLineWeekDay : "boolean",
						isShowClear : "boolean",
						isShowToday : "boolean",
						isShowOthers : "boolean",
						readOnly : "boolean",
						errDealMode : "boolean",
						autoPickDate : "boolean",
						qsEnabled : "boolean",
						autoShowQS : "boolean",
						opposite : "boolean"
					}
				]));
	};
	$.fn.my97.defaults = {
		dateFmt : 'yyyy-MM-dd HH:mm:ss'
	};

	$.parser.plugins.push('my97');
})(jQuery);

/**
 * 扩展树表格级联勾选方法：
 * $(this).treegrid('cascadeCheck',{  
 *  id:row.id, //节点ID  
 *  deepCascade:true //深度级联  
 * }); 
 */
$.extend($.fn.treegrid.methods,{
	/**
	 * 级联选择
     * @param {Object} target
     * @param {Object} param 
	 *		param包括两个参数:
     *			id:勾选的节点ID
     *			deepCascade:是否深度级联
     * @return {TypeName} 
	 */
	cascadeCheck : function(target,param){
		var opts = $.data(target[0], "treegrid").options;
		if(opts.singleSelect)
			return;
		var idField = opts.idField;//这里的idField其实就是API里方法的id参数
		var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选
		var selectNodes = $(target).treegrid('getSelections');//获取当前选中项
		for(var i=0;i<selectNodes.length;i++){
			if(selectNodes[i][idField]==param.id)
				status = true;
		}
		//级联选择父节点
		selectParent(target[0],param.id,idField,status);
		selectChildren(target[0],param.id,idField,param.deepCascade,status);
		/**
		 * 级联选择父节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectParent(target,id,idField,status){
			var parent = $(target).treegrid('getParent',id);
			if(parent){
				var parentId = parent[idField];
				if(status)
					$(target).treegrid('select',parentId);
				else
					$(target).treegrid('unselect',parentId);
				selectParent(target,parentId,idField,status);
			}
		}
		/**
		 * 级联选择子节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} deepCascade 是否深度级联
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectChildren(target,id,idField,deepCascade,status){
			//深度级联时先展开节点
			if(!status&&deepCascade)
				$(target).treegrid('expand',id);
			//根据ID获取下层孩子节点
			var children = $(target).treegrid('getChildren',id);
			for(var i=0;i<children.length;i++){
				var childId = children[i][idField];
				if(status)
					$(target).treegrid('select',childId);
				else
					$(target).treegrid('unselect',childId);
				selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点
			}
		}
	}
});
/*
 * easyui-treegrid客户端分页
 */
(function($){
	function pagerFilter(data){
        if ($.isArray(data)){    // is array  
            data = {  
                total: data.length,  
                rows: data  
            };  
        }
        var dg = $(this);  
		var state = dg.data('treegrid');
        var opts = dg.treegrid('options');  
        var pager = dg.treegrid('getPager');  
        pager.pagination({  
            onSelectPage:function(pageNum, pageSize){  
                opts.pageNumber = pageNum;  
                opts.pageSize = pageSize;  
                pager.pagination('refresh',{  
                    pageNumber:pageNum,  
                    pageSize:pageSize  
                });  
                dg.treegrid('loadData',state.originalRows);  
            }  
        });  
        if (!state.originalRows){
        	state.originalRows = data.rows;
        }
        var topRows = [];
        var childRows = [];
        $.map(state.originalRows, function(row){
        	row._parentId ? childRows.push(row) : topRows.push(row);
        });
        data.total = topRows.length;
        var start = (opts.pageNumber-1)*parseInt(opts.pageSize);  
        var end = start + parseInt(opts.pageSize);  
		data.rows = $.extend(true,[],topRows.slice(start, end).concat(childRows));
		return data;
	}

	var appendMethod = $.fn.treegrid.methods.append;
	var loadDataMethod = $.fn.treegrid.methods.loadData;
	$.extend($.fn.treegrid.methods, {
		clientPaging: function(jq){
			return jq.each(function(){
				var state = $(this).data('treegrid');
				var opts = state.options;
				opts.loadFilter = pagerFilter;
				var onBeforeLoad = opts.onBeforeLoad;
				opts.onBeforeLoad = function(row,param){
					state.originalRows = null;
					onBeforeLoad.call(this, row, param);
				};
				$(this).treegrid('loadData', state.data);
				$(this).treegrid('reload');
			});
		},
		loadData: function(jq, data){
			jq.each(function(){
				$(this).data('treegrid').originalRows = null;
			});
			return loadDataMethod.call($.fn.treegrid.methods, jq, data);
		},
		append: function(jq, param){
			return jq.each(function(){
				var state = $(this).data('treegrid');
				if (state.options.loadFilter == pagerFilter){
					$.map(param.data, function(row){
						row._parentId = row._parentId || param.parent;
						state.originalRows.push(row);
					});
					$(this).treegrid('loadData', state.originalRows);
				} else {
					appendMethod.call($.fn.treegrid.methods, jq, param);
				}
			});
		}
	});

})(jQuery);