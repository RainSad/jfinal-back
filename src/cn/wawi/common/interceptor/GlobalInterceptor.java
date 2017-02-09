package cn.wawi.common.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import cn.wawi.common.annotation.Permission;
import cn.wawi.model.sys.Resources;
import cn.wawi.model.sys.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * @description 全局权限拦截
 * @author 龚亮
 * @date 2015-05-26 09:49:19
 */
public class GlobalInterceptor implements Interceptor{
	
	
	/**
	 * 权限拦截
	 */
	@SuppressWarnings("all")
	public void intercept(Invocation invocation){
		boolean flag=false;
		Controller c=invocation.getController();
		Method method=invocation.getMethod();
		Permission permission = method.getAnnotation(Permission.class);
		User user=c.getSessionAttr("loginUser");
		UserAgent userAgent = UserAgent.parseUserAgentString(c.getRequest().getHeader("User-Agent")); 
		if(userAgent.getOperatingSystem().isMobileDevice()){
			invocation.invoke();
		}else{
			if(user==null){
			 	c.redirect("/");
			}else{
				flag=permission==null?true:hasPermission(c,permission);
				if(flag){
					invocation.invoke();
				}else{
					c.renderFreeMarker("/public/error/noPermission.html");
				}
			}
		}
	}
	
	/**
	 * 权限验证
	 */
	public boolean hasPermission(Controller c,Permission permission){
		boolean flag=false;
		String[] values=permission.value();
		List<Resources> list=c.getSessionAttr("menuList");
		if(!StrKit.notBlank(values)){
			return true;
		}
		for(Resources record: list){
			for(String p: values){
				if(record.getPermCode().contains(p)){
					flag=true;
					break;
				}
			}
		}
		return flag;
	}
}
