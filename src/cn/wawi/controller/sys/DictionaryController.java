package cn.wawi.controller.sys;

import cn.wawi.common.base.Conditions;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Dictionary;
import org.apache.commons.lang3.StringUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey="/sys_dictionary")
public class DictionaryController extends BaseController<Dictionary>{
	
	@Override
	public String getSql() {
		Conditions condi = new Conditions();
		Dictionary dict=new Dictionary();
		dict.setStatus(getPara("status", "1"));
    	condi.setFiledQuery(Conditions.EQUAL,"status");
    	condi.modelToCondition(dict,"d");
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select d.* from sys_dictionary d "+condi.getSql();
    	if(StrKit.notBlank(getPara("type"))){
    		params.add(getPara("type"));
    		if(StringUtils.containsIgnoreCase(sql, "where")){
    			sql+="AND d.parent_id in (select id from sys_dictionary d1 where d1.type=?) ";
    		}else{
    			sql+="WHERE d.parent_id in (select id from sys_dictionary d1 where d1.type=?) ";
    		}
    	}
		return sql;
	}
}
