package cn.wawi.model.sys;

import java.util.Date;
import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import cn.wawi.common.base.Conditions;
import cn.wawi.model.sys.base.BaseResources;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Resources extends BaseResources<Resources> {
	public static final Resources dao = new Resources();
	public Resources(){
		this.setCreateTime(new Date());
	}
	public Resources(String name,String permCode,String iconCls,int parentId,int level,int sort){
		this.setStatus("1");
		this.setName(name);
		this.setPermCode(permCode);
		this.setIconCls(iconCls);
		this.setParentId(parentId);
		this.setType("2");
		this.setLevel(level);
        this.setSort(sort);
	}
	/**
	 * 获取用户权限
	 * @param userId 用户id
	 * @param type菜单类型  1 菜单 2 权限
	 * @return 权限列表
	 */
	public List<Record> findUserPermission(int userId,int type) {
    	Conditions condi = new Conditions();
    	Record r=new Record();
    	r.set("user_id", userId);
    	r.set("type", type);
    	condi.recordToCondition(r);
		return Db.find("select * from v_user_role_resource "+condi.getSql(),condi.getParamList().toArray());
	}
	
	public List<Record> getRoleResources(int roleId){
		return Db.find("SELECT resources_id id from sys_role_resources where role_id=?",roleId);
	}
	
}
