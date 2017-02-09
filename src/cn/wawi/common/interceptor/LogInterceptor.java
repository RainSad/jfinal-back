package cn.wawi.common.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import cn.wawi.common.annotation.Logs;
import cn.wawi.common.base.R;
import cn.wawi.model.sys.Log;
import cn.wawi.model.sys.User;
import cn.wawi.utils.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.render.JsonRender;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @description 全局异常处理及日志拦截
 * @author 龚亮
 * @date 2015-05-26 09:49:19
 */
public class LogInterceptor implements Interceptor{
	
	R<?> r=new R<>(1,"请求成功");
	
	/**
	 * 日志记录及异常拦截
	 */
	@SuppressWarnings("all")
	public void intercept(Invocation invocation){
		Controller c=invocation.getController();
		Method method=invocation.getMethod();
		User user=c.getSessionAttr("loginUser");
		UserAgent userAgent = UserAgent.parseUserAgentString(c.getRequest().getHeader("User-Agent")); 
		try {
			invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
			r.setResCode(0);
			r.setResMsg("请求失败!");
			/**
			 * json异常处理
			 */
			if(c.getRender() instanceof com.jfinal.render.JsonRender){
				c.render(new JsonRender(r).forIE());
			}
		}finally{
			/**
			 * 日志记录
			 */
			if(method.isAnnotationPresent(Logs.class)&&user!=null){
				HttpServletRequest request= c.getRequest();
				Logs logs = method.getAnnotation(Logs.class);
				Log log=new Log();
				log.setBrowser(userAgent.getBrowser().getName());
				log.setCreateTime(new Date());
				log.setIp(StringUtil.getIpAddr(request));
				log.setOs(userAgent.getOperatingSystem().getName());
				log.setRemark(logs.des());
				if(user!=null){
					log.setOptUser(user.getUsername());
					log.setRealname(user.getRealname());
				}
				HashMap map=new HashMap(request.getParameterMap()); 
				if(map.containsKey("password")){
					map.put("password", new String[]{"******"});
				}
				if(map.containsKey("newPassword")){
					map.put("newPassword", new String[]{"******"});
				}
				log.setReqParam(JsonKit.toJson(map));
				log.set("requestParam", JsonKit.toJson(map));
				log.setReqUrl(request.getRequestURI());
				log.setIsSuccess(r.getResCode()+"");
				log.save();
			}
		}
	}
}
