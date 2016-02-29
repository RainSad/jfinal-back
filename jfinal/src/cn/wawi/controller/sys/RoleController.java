package cn.wawi.controller.sys;

import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;
import cn.wawi.common.annotation.Permission;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.Role;

@ControllerBind(controllerKey="/sys_role")
public class RoleController extends BaseController<Role> {

	public void list(){
		render(new JsonRender(findAllByPage()).forIE());
	}
	/*
	 * 查询权限列表
	 */
	public void pList(){
		List<Privilege> list=Privilege.dao.findAll();
        map.put("rows",list);
        map.put("total",list.size());
		render(new JsonRender(map).forIE());
	}
	/*
	 * 获取角色对应的权限
	 */
	public void getPrivilege(){
		render(new JsonRender(Db.find("select privilegeId id from sys_role_privilege where roleId=?",getPara("id"))).forIE());
	}
	/*
	 * 保存角色对应的权限
	 */
	@Before(Tx.class)
	public void saveRolePrivilege(){
		String roleId=getPara("roleId");
		Db.update("delete from sys_role_privilege where roleId=?", roleId);
		String privilegeIds=getPara("privilegeIds");
		if(privilegeIds!=null&&!"".equals(privilegeIds.trim())){
			String[] privileges=privilegeIds.split(",");
			Object[][] o=new Object[privileges.length][2];
			for (int i = 0; i < o.length; i++) {
				o[i][0]=roleId;
				o[i][1]=privileges[i];
			}
			Db.batch("insert into sys_role_privilege(roleId,privilegeId) values(?,?)",o, 1000);
		}
		render(new JsonRender(map).forIE());
	}
	/*
	 * 获取用户对应角色
	 */
	@Permission("sys:user:roleView")
	public void getUserRole(){
		render(new JsonRender(Db.find("select roleId id from sys_user_role where userId=?",getPara("userId"))).forIE());
	}
	/*
	 * 保存用户对应角色
	 */
	@Before(Tx.class)
	public void saveUserRole(){
		String userId=getPara("userId");
		Db.update("delete from sys_user_role where userId=?", userId);
		String roleId=getPara("roleId");
		if(roleId!=null&&!"".equals(roleId.trim())){
			String[] roles=roleId.split(",");
			Object[][] o=new Object[roles.length][2];
			for (int i = 0; i < o.length; i++) {
				o[i][0]=userId;
				o[i][1]=roles[i];
			}
			Db.batch("insert into sys_user_role(userId,roleId) values(?,?)",o, 1000);
		}
		render(new JsonRender(map).forIE());
	}
	@Override
	public String getSql() {
		return "select * from sys_role";
	}
}
