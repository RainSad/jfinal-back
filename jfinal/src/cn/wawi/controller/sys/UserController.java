package cn.wawi.controller.sys;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.JsonRender;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.User;
import cn.wawi.utils.StringUtil;

@ControllerBind(controllerKey="/sys_user")
public class UserController extends BaseController<User> {

	public void list(){
		render(new JsonRender(findAllByPage()).forIE());
	}
	public void login() {
        if(!getPara("captcha").equalsIgnoreCase((String) getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY))){
        	setAttr("msg", "验证码错误!");
        	renderFreeMarker("/login.html");
        }else{
            User user=getSessionAttr("loginUser");
            if(user==null){
            	user=User.dao.login(getPara("username"), getPara("password"));
            	if(user==null){
                	setAttr("msg", "用户名或密码不对!");
                	renderFreeMarker("/login.html");
            	}else{
            		setSessionAttr("loginUser", user);
            		setAttr("permissions", Privilege.dao.findUserPermission(user.get("id"),"F"));
            		renderFreeMarker("/index.html");
            	}
            }
            setAttr("permissions", Privilege.dao.findUserPermission(user.get("id"),"F"));
            renderFreeMarker("/index.html");
        }
	}
	
	public void exit(){
		getSession().removeAttribute("loginUser");
		getSession().invalidate();
		renderFreeMarker("/login.html");
	}
	
	public String getSql(){
		StringBuilder sb=new StringBuilder("");
		sb.append("select u.*,d.name from sys_user u LEFT JOIN sys_department d  on u.departmentId=d.id");
		if(StringUtil.notBlank(getPara("name"))){
			sb.append(" where (");
			sb.append(" username like ? ");
			sb.append(" OR realname like ? ");
			sb.append(" OR email like ? ");
			sb.append(" OR phone like ? ");
			String name=getPara("name");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			sb.append(" ) ");
		}
		buildSql(sb);
		return sb.toString();
	}
}
