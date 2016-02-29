package cn.wawi.controller.sys;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.JsonRender;
import cn.wawi.common.annotation.Permission;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.utils.DbUtil;
import cn.wawi.utils.StringUtil;

@ControllerBind(controllerKey="/sys_privilege")
public class PrivilegeController extends BaseController<Privilege> {

	/*
	 * 权限列表
	 */
	public void list(){
		render(new JsonRender(findAllByPage()).forIE());
	}
	/*
	 * 菜单列表
	 */
	public void menuList(){
		render(new JsonRender(findAll("select * from sys_privilege where type='F'")).forIE());
	}
	/*
	 * 添加基本权限
	 */
	@Permission("sys:role:savePrivilege")
	public void addPermission(){
		int[] i=Db.batch("insert into sys_privilege(name,perm_code,description,parentId,sort,type)", new Object[][]{{"添加 ","add","拥有添加权限",getPara("pId"),"99","O"},{"删除 ","delete","拥有删除权限",getPara("pId"),"99","O"},{"修改 ","update","拥有修改权限",getPara("pId"),"99","O"},{"查看 ","view","拥有查看权限",getPara("pId"),"99","O"}}, 1000);
		if(i==null||i.length<0){
			map.put("resMsg", "添加基本权限失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}
	/*
	 * 获取菜单树
	 */
	public void getMenu(){
		render(new JsonRender(DbUtil.findTree(Db.find("select * from sys_privilege where type='F'"))).forIE());
	}
	/*
	 * 跳转到菜单页面
	 */
	@Permission("sys:privilege:menu:view")
	public void menu(){
		renderFreeMarker("/views/sys/Privilege/menu.html");
	}
	public String getSql(){
		StringBuilder sb=new StringBuilder("");
		sb.append("select * from sys_privilege where type='O'");
		if(StringUtil.notBlank(getPara("parentId"))){
			sb.append(" AND parentId=? ");
			setParams(getPara("parentId"));
		}
		return sb.toString();
	}
}
