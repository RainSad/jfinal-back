package cn.wawi.model.sys;


import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.wawi.model.sys.base.BaseRole;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Role extends BaseRole<Role> {
	public static final Role dao = new Role();
	
	public List<Record> getPrivilege(String roleId){
		return Db.find("select privilegeId id from sys_role_privilege where roleId=?",roleId);
	}
	public List<Record> getUserRole(String userId){
		return Db.find("select roleId id from sys_user_role where userId=?",userId);
	}
}
