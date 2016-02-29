package cn.wawi.controller.sys;

import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Department;
import cn.wawi.utils.DbUtil;

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

}
