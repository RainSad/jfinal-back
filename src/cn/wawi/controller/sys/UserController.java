package cn.wawi.controller.sys;

import cn.wawi.common.interceptor.Conditions;
import cn.wawi.common.interceptor.GlobalInterceptor;
import cn.wawi.common.interceptor.LogInterceptor;
import cn.wawi.common.interceptor.LoginInterceptor;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.User;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HashKit;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_user")
public class UserController extends BaseController<User>{

	
	@Clear({GlobalInterceptor.class,LogInterceptor.class})
	@Before(LoginInterceptor.class)
	public void login() {
		User user=getSessionAttr("loginUser");
    	if(user==null){
        	user=User.dao.login(getPara("username"), getPara("password"));
        	if(user==null){
            	setAttr("msg", "用户名或密码不对!");
            	renderFreeMarker("/login.html");
        	}
        	setSessionAttr("loginUser", user);
    	}
		setAttr("permissions", Privilege.dao.findUserPermission(user.get("id")));
		renderFreeMarker("/index.html");
	}
	public void exit(){
		getSession().removeAttribute("loginUser");
		getSession().invalidate();
		renderFreeMarker("/login.html");
	}
	public void initPwd(){
		User user=new User();
		user.setId(getParaToInt("id"));
		user.set("password", HashKit.md5("123456"));
		if(!user.update()){
			json.setResMsg("修改失败!");
			json.setResCode(0);
		}else{
			List<User> list=new ArrayList<User>();
			list.add(user);
			json.setRows(list);
			json.setTotal(1L);
		}
		render(new JsonRender(json).forIE());
	}
	@Override
	public String getSql() {
		Conditions condi = new Conditions();
		User user=new User();
		user.setEmail(getPara("name"));
		user.setUsername(getPara("name"));
		user.setRealname(getPara("name"));
		user.setStatus(getPara("status"));
		user.setPhone(getPara("name"));
		user.setInputTime(null);
    	condi.setValueQuery(Conditions.GREATER_EQUAL, "inputTime", getPara("startTime")); //开始时间
    	condi.setValueQuery(Conditions.LESS_EQUAL, "inputTime", getPara("endTime")); //结束时间
    	condi.setFiledQuery(Conditions.FUZZY, "phone","realname","username","email");
    	condi.modelToCondition(user,"u");
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select u.*,d.name from "+tablename +" u left JOIN sys_department d on u.departmentId=d.id ";
		return sql+condi.getSql();
	}
}
