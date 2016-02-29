package cn.wawi.common;

import cn.wawi.common.interceptor.GlobalInterceptor;
import cn.wawi.utils.SystemUtils;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**
 * API引导式配置
 */
public class DefaultConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("jdbc.properties");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
		me.setFreeMarkerTemplateUpdateDelay(1);
		me.setError500View("/error/500.html");
		me.setError403View("/error/403.html");
		me.setError404View("/error/404.html");
		me.setError401View("/error/401.html");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("url"), getProperty("username"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		/* 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("sys_user", User.class);	*/
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(c3p0Plugin);
		atbp.setShowSql(true);
		//atbp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//不区分大小写
		atbp.setContainerFactory(new PropertyNameContainerFactory());//命名转换
		me.add(atbp);
		
		//me.add(new SqlInXmlPlugin());
		
		//me.add(new QuartzPlugin("job.properties"));//开启定时任务
		
		// ehcache缓存插件
		//me.add(new EhCachePlugin());
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		 //全局异常拦截及系统日志记录
	     me.add(new GlobalInterceptor());
	 	 // 添加事物，对save、update和delete自动进行拦截
		 me.add(new TxByMethods("save", "update", "delete","deleteBatch","updateBatch"));
	     me.add(new SessionInViewInterceptor());
	}
	
	/**
	 * 配置全局处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
		//me.add(new CacheControlHeaderHandler(1));
	}

	public static void main(String[] args) {
		JFinal.start("WebContent", 8090, SystemUtils.getRootPath(), 5);
	}
}

