package cn.wawi.common;

import cn.wawi.model.sys.Department;
import cn.wawi.model.sys.Dict;
import cn.wawi.model.sys.Dictitem;
import cn.wawi.model.sys.Log;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.Role;
import cn.wawi.model.sys.User;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;


public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("sys_department", "id", Department.class);
		arp.addMapping("sys_dict", "id", Dict.class);
		arp.addMapping("sys_dictitem", "id", Dictitem.class);
		arp.addMapping("sys_log", "id", Log.class);
		arp.addMapping("sys_privilege", "id", Privilege.class);
		arp.addMapping("sys_role", "id", Role.class);
		arp.addMapping("sys_user", "id", User.class);
	}
}

