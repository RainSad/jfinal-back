package cn.wawi.common;

import cn.wawi.model.data.Badminton;
import cn.wawi.model.data.Broadband;
import cn.wawi.model.data.Coach;
import cn.wawi.model.data.Community;
import cn.wawi.model.data.CourseEnrol;
import cn.wawi.model.data.GroupCoach;
import cn.wawi.model.data.Residence;
import cn.wawi.model.data.Sales;
import cn.wawi.model.data.WeixinUser;
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
		arp.addMapping("k_broadband", "broadband_id", Broadband.class);
		arp.addMapping("k_community", "community_id", Community.class);
		arp.addMapping("k_residence", "residence_id", Residence.class);
		arp.addMapping("k_weixin_user", "user_id", WeixinUser.class);
		arp.addMapping("y_badminton", "id", Badminton.class);
		arp.addMapping("y_coach", "id", Coach.class);
		arp.addMapping("y_course_enrol", "id", CourseEnrol.class);
		arp.addMapping("y_sales", "id", Sales.class);
		arp.addMapping("y_group_coach", "id", GroupCoach.class);
	}
}

