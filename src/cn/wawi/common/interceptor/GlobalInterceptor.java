package cn.wawi.common.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import cn.wawi.common.annotation.Logs;
import cn.wawi.common.annotation.Permission;
import cn.wawi.model.sys.User;
import cn.wawi.utils.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @description 全局异常处理及日志拦截
 * @author 龚亮
 * @date 2015-05-26 09:49:19
 */
public class GlobalInterceptor implements Interceptor{
	
	int resCode=1;
	String resMsg="请求成功!";
	
	/**
	 * 日志记录及异常拦截及权限拦截
	 */
	@SuppressWarnings("all")
	public void intercept(Invocation invocation){
		boolean flag=false;
		Controller c=invocation.getController();
		Method method=invocation.getMethod();
		Permission permission = method.getAnnotation(Permission.class);
		User user=c.getSessionAttr("loginUser");
		try {
			if(user==null){
			 	c.renderFreeMarker("/login.html");
			}else{
				flag=permission==null?true:hasPermission(user,permission.value());
				if(flag){
					invocation.invoke();
				}else{
					c.renderFreeMarker("/error/noPermission.html");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resCode=0;
			resMsg="请求失败!";
			/**
			 * json异常处理
			 */
			if(c.getRender() instanceof com.jfinal.render.JsonRender){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("resMsg", resMsg);
				map.put("resCode", resCode);
				c.render(new JsonRender(map).forIE());
			}
		}finally{
			/**
			 * 日志记录
			 */
			if(method.isAnnotationPresent(Logs.class)&&user!=null){
				HttpServletRequest request= c.getRequest();
				Logs logs = method.getAnnotation(Logs.class);
				UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
				Record log=new Record();
				log.set("optUser", user.getStr("username"));
				log.set("realname",  user.getStr("realname"));
				log.set("description", logs.des());
				log.set("os", userAgent.getOperatingSystem().getName());
				log.set("ip", StringUtil.getIpAddr(request));
				log.set("browser", userAgent.getBrowser().getName());
				log.set("inputTime", new Date());
				HashMap map=new HashMap(request.getParameterMap()); 
				if(map.containsKey("password")){
					map.put("password", new String[]{"******"});
				}
				log.set("requestParam", JsonKit.toJson(map));
				log.set("operationCode", request.getRequestURI());
				log.set("isSuccess", resCode);
				Db.save("sys_log", log);
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
