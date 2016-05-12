package cn.wawi.common.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import cn.wawi.common.annotation.Permission;
import cn.wawi.model.sys.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @description 全局权限拦截
 * @author 龚亮
 * @date 2015-05-26 09:49:19
 */
public class GlobalInterceptor implements Interceptor{
	
	int resCode=1;
	String resMsg="请求成功!";
	
	/**
	 * 日志记录及权限拦截
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
			 	c.renderFreeMarker("/login.html");
			}else{
				flag=permission==null?true:hasPermission(user,permission.value());
				if(flag){
					invocation.invoke();
				}else{
					c.renderFreeMarker("/common/noPermission.html");
				}
			}
		}
	}
	
	/**
	 * 权限验证
	 */
	public boolean hasPermission(User user,String permission){
		boolean flag=false;
		List<Record> list=Db.find("select permCode from sys_module where userId=? and type='O'",new Object[]{user.getId()});
		for(Record record: list){
			if(record.getStr("permCode").contains(permission)){
				flag=true;
				break;
			}
		}
		return flag;
	}
}
