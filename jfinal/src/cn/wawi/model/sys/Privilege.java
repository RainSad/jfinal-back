package cn.wawi.model.sys;

import java.util.List;
import cn.wawi.utils.DbUtil;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName = "sys_privilege")
public class Privilege extends ModelExt<Privilege> {

	private static final long serialVersionUID = 1L;
	public static final Privilege dao = new Privilege();

	/*
	 * 查询登录用户的权限-树
	 */
	public List<Record> findUserPermission(Object userId,String type){
		if("O".equals(type)){
			return Db.find("select perm_code permCode from sys_module where userId=? and type='O'",new Object[]{userId});
		}
		return DbUtil.findTree(Db.find("select id,iconCls,name,url,description,parentId,perm_code permCode from sys_module where userId=? and type='F'",new Object[]{userId}));
	}
}
