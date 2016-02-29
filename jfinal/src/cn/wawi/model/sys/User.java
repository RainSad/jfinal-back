package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_user")
public class User extends ModelExt<User>{
	
	private static final long serialVersionUID = 1L;
	
	public static final User dao=new User();
	
	/*
	 *  用户登录
	 */
	public User login(String username,String password){
		return findFirst("select * from sys_user where username=? and password=?",new Object[]{username,password});
	}
}
