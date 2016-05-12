package cn.wawi.controller.sys;

import cn.wawi.common.interceptor.Conditions;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Dict;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/sys_dict")
public class DictController extends BaseController<Dict>{

	@Override
	public String getSql() {
		System.out.println(getPara("name"));
		Conditions condi = new Conditions();
		Dict dict=new Dict();
		dict.setStatus(getPara("status"));
		dict.setType(getPara("name"));
		dict.setValue(getPara("name"));
		dict.setDescription(getPara("name"));
		dict.setLabel(getPara("name"));
    	condi.setFiledQuery(Conditions.FUZZY, "label","value","type","description");
    	condi.modelToCondition(dict);
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select * from "+tablename;
		return sql+condi.getSql();
	}
}
