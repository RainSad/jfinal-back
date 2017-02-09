package cn.wawi.controller.sys;

import cn.wawi.common.base.R;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Role;
import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_role")
public class RoleController extends BaseController<Role>{
	
	@Override
	public String getSql() {
    	String sql="select * from "+tablename;
		return sql;
	}
	
	public void getUserRole(){
		R<Role> r=new R<Role>(1,"查询用户角色成功!");
		int userId=getParaToInt("userId");
		List<Role> roles=Role.dao.getUserRole(userId);
		r.put(roles);
        render(new JsonRender(r).forIE());
	}
	@Before(Tx.class)
	public void saveUserRole(){
		R<Record> r=new R<Record>(1,"保存用户角色成功!");
		int userId=getParaToInt("userId");
		Integer[] roleIds=getParaValuesToInt("roleIds[]");
		Db.update("delete from sys_user_role where user_id=?",userId);
		for(int roleId: roleIds){
			Record record=new Record();
			record.set("user_id", userId);
			record.set("role_id", roleId);
			Db.save("sys_user_role", record);
		}
        render(new JsonRender(r).forIE());
	}
}
