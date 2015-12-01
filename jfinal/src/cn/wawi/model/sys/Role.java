package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_role")
public class Role extends ModelExt<Role>{
	
	private static final long serialVersionUID = 1L;
	public static final Role role=new Role();
}
