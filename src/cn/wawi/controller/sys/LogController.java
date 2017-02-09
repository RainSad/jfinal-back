package cn.wawi.controller.sys;

import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Log;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/sys_log")
public class LogController extends BaseController<Log>{
	
	@Override
	public String getSql() {
    	String sql="select * from "+tablename;
		return sql;
	}
}
