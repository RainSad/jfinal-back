package cn.wawi.common.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import cn.wawi.common.annotation.Logs;
import cn.wawi.model.sys.User;
import cn.wawi.utils.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 登录拦截器
 * @author 龚亮
 */
public class LoginInterceptor implements Interceptor{
	
	int resCode=1;
	
	@SuppressWarnings("all")
	public void intercept(Invocation invocation) {
		Controller c=invocation.getController();
		Method method=invocation.getMethod();
		String captcha=c.getPara("captcha");
		String kaptcha=(String) c.getSession().getAttribute("kaptcha");
		if(StrKit.notBlank(captcha,kaptcha)&&kaptcha.equalsIgnoreCase(captcha)){
			try {
				invocation.invoke();
			} catch (Exception e) {
				e.printStackTrace();
			    resCode=0;
			}finally{
				/**
				 * 日志记录
				 */
				User user=c.getSessionAttr("loginUser");
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
		}else{
        	c.setAttr("msg", "验证码不对!");
        	c.renderFreeMarker("/login.html");
		}

	}

}
