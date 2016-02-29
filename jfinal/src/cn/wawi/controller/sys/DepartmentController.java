package cn.wawi.controller.sys;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.JsonRender;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Department;
import cn.wawi.utils.DbUtil;

@ControllerBind(controllerKey="/sys_department")
public class DepartmentController extends BaseController<Department> {

	private String sql="select * from sys_department";
	/*
	 * 部门列表
	 */
	public void list(){
		render(new JsonRender(findAll()).forIE());
	}
	/*
	 * 获取部门树
	 */
	public void tree(){
		render(new JsonRender(DbUtil.findTree(Db.find(sql))).forIE());
	}
	@Override
	public String getSql() {
		return sql;
	}
}
