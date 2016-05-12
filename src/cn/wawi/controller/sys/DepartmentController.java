package cn.wawi.controller.sys;

import cn.wawi.common.interceptor.Conditions;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Department;
import cn.wawi.utils.DbUtil;
import cn.wawi.utils.StringUtil;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_department")
public class DepartmentController extends BaseController<Department>{
	
	/*
	 * 获取部门树
	 */
	public void tree(){
		render(new JsonRender(DbUtil.findTree(Db.find("select * from sys_department"))).forIE());
	}
	@Override
	public String getSql() {
		Conditions condi = new Conditions();
		Department dept=new Department();
		dept.setStatus(getPara("status"));
		dept.setAddress(getPara("name"));
		dept.setDescription(getPara("name"));
		dept.setParentId(StringUtil.parseInteger(getPara("parentId")));
    	condi.setFiledQuery(Conditions.FUZZY, "name","description","address");
    	condi.modelToCondition(dept);
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select * from "+tablename;
		return sql+condi.getSql();
	}
}
