package cn.wawi.controller.sys;

import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.Conditions;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Role;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_role")
public class RoleController extends BaseController<Role>{

	/*
	 * 获取角色对应的权限
	 */
	public void getPrivilege(){
		render(new JsonRender(Role.dao.getPrivilege(getPara("id"))).forIE());
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
		render(new JsonRender(json).forIE());
	}
	/*
	 * 获取用户对应角色
	 */
	@Permission("sys:user:roleView")
	public void getUserRole(){
		render(new JsonRender(Role.dao.getUserRole(getPara("userId"))).forIE());
	}
	/*
	 * 保存用户对应角色
	 */
	@Before(Tx.class)
	public void saveUserRole(){
		String userId=getPara("userId");
		Db.update("delete from sys_user_role where userId=?", userId);
		String roleId=getPara("roleId");
		System.out.println(roleId+":"+getPara("userId"));
		if(roleId!=null&&!"".equals(roleId.trim())){
			String[] roles=roleId.split(",");
			Object[][] o=new Object[roles.length][2];
			for (int i = 0; i < o.length; i++) {
				o[i][0]=userId;
				o[i][1]=roles[i];
			}
			Db.batch("insert into sys_user_role(userId,roleId) values(?,?)",o, 1000);
		}
		render(new JsonRender(json).forIE());
	}
	@Override
	public String getSql() {
		Conditions condi = new Conditions();
		Role role=new Role();
		role.setName(getPara("name"));
		role.setStatus(getPara("status"));
    	condi.setFiledQuery(Conditions.FUZZY, "name");
    	condi.modelToCondition(role);
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select * from "+tablename;
		return sql+condi.getSql();
	}
}
