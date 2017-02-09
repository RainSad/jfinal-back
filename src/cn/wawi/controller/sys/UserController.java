package cn.wawi.controller.sys;

import cn.wawi.common.base.Conditions;
import cn.wawi.common.base.R;
import cn.wawi.common.interceptor.GlobalInterceptor;
import cn.wawi.common.interceptor.LoginValidate;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Resources;
import cn.wawi.model.sys.User;
import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_user")
public class UserController extends BaseController<User>{

   public void getUserPermission(){
	    R<Record> r=new R<Record>(1,"查找用户权限成功!");
		User user=getUser();
		List<Record> menuLis=getSessionAttr("menuList");
		Record extra=new Record();
		if(null==menuLis&&user!=null){
			extra.set("username", user.getUsername());
			extra.set("realname", user.getRealname());
			menuLis=Resources.dao.findUserPermission(user.getId(),1);
			setSessionAttr("menuList", menuLis);
		}
		r.setExtra(extra);
		render(new JsonRender(r.put(menuLis)).forIE());
	}
	@Before(LoginValidate.class)
	@Clear(GlobalInterceptor.class)
	public void login() {
		User user=getUser();
    	if(user==null){
        	user=User.dao.login(getPara("username"), getPara("password"));
        	if(user!=null){
        		setSessionAttr("loginUser", user);	
        		redirect("/index");
        	}else{
            	setAttr("msg", "用户名或密码不对!");
            	renderFreeMarker("/login.html");
        	}
    	}else{
    		renderFreeMarker("/index.html");
    	}
	}
	@ActionKey("/index")
	public void index(){
		User user=getUser();
		if(user==null){
			setAttr("msg", "session过期!");
			renderFreeMarker("/login.html");
		}else{
			renderFreeMarker("/index.html");
		}
	}
	@ActionKey("/logout")
	public void logout(){
		System.out.println("退出系统!");
		getSession().removeAttribute("loginUser");
		getSession().removeAttribute("menuList");
		getSession().invalidate();
		renderFreeMarker("/login.html");
	}
	public void updatePassword(){
		User user=getUser();
		if(StrKit.isBlank(getPara("newPassword"))){
			render(new JsonRender("新密码不能为空!").forIE());
			return;
		}
		user.set("password", HashKit.md5(HashKit.sha1(user.getUsername()+getPara("newPassword"))));
		if(!user.update()){
			render(new JsonRender(error("修改密码失败!")).forIE());
		}else{
			render(new JsonRender(success("修改密码成功!")).forIE());
		}
		
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
		user.setRegTime(null);
    	condi.setValueQuery(Conditions.GREATER_EQUAL, "inputTime", getPara("startTime")); //开始时间
    	condi.setValueQuery(Conditions.LESS_EQUAL, "inputTime", getPara("endTime")); //结束时间
    	condi.setFiledQuery(Conditions.EQUAL, "phone","realname","username","email");
    	condi.modelToCondition(user,"u");
    	params.clear();
    	params.addAll(condi.getParamList());
    	String sql="select u.*,d.dept_name from sys_user u left join sys_department d on u.dept_id=d.id  ";
		return sql+condi.getSql();
	}
}
