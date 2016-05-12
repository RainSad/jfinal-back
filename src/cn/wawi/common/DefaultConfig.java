package cn.wawi.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import cn.wawi.common.interceptor.LogInterceptor;

/**
 * API引导式配置
 */
public class DefaultConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("jdbc.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setJsonFactory(new JacksonFactory());
		me.setViewType(ViewType.FREE_MARKER);
		me.setFreeMarkerTemplateUpdateDelay(1);
        me.setBaseUploadPath("d:/images/weixin/");
		me.setError500View("/common/500.html");
		me.setError403View("/common/403.html");
		me.setError404View("/common/404.html");
		me.setError401View("/common/401.html");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
	}
	
	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("url"), PropKit.get("username"), PropKit.get("password").trim(),PropKit.get("driver"));
	}
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("url"), PropKit.get("username"), PropKit.get("password").trim(),PropKit.get("driver"));
	}
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		
//		DruidPlugin druidPlugin=createDruidPlugin();
//		druidPlugin.addFilter(new StatFilter());
//		WallFilter wall = new WallFilter();
//		wall.setDbType("mysql");
//		druidPlugin.addFilter(wall);
//		me.add(druidPlugin);
		// 配置C3p0数据库连接池插件
		C3p0Plugin C3p0Plugin = createC3p0Plugin();
		me.add(C3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
		arp.setShowSql(true);
		me.add(arp);
		
		// 所有配置在 MappingKit 中搞定
		_MappingKit.mapping(arp);
		
		//SchedulerPlugin sp = new SchedulerPlugin("job.properties");
		//me.add(sp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	     me.add(new SessionInViewInterceptor());
	     me.add(new LogInterceptor());
	     //me.add(new GlobalInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
	}
	
	public static void main(String[] args) {
		JFinal.start("WebContent", 8080, "/weixin", 5);
	}
}
