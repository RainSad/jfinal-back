package cn.wawi.controller.sys;

import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Department;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/sys_dept")
public class DepartmentController extends BaseController<Department>{
	
	@Override
	public String getSql() {
    	String sql="select * from "+tablename;
		return sql;
	}
}
