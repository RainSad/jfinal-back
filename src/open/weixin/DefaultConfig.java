package open.weixin;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.scheduler.SchedulerPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.upload.OreillyCos;

import cn.wawi.common.base.RandomFileRenamePolicy;
import cn.wawi.common.interceptor.GlobalInterceptor;

/**
 * API引导式配置
 */
public class DefaultConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		PropKit.use("jdbc.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
        me.setBaseUploadPath(PropKit.get("fileUploadPath"));
		me.setError500View("/public/error/500.html");
		me.setError403View("/public/error/403.html");
		me.setError404View("/public/error/404.html");
		me.setError401View("/public/error/401.html");
		me.setJsonFactory(new JacksonFactory());
		me.setJsonDatePattern("yyyy-MM-dd HH:mm:ss");
	}
	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("url"), PropKit.get("username"), PropKit.get("password").trim(),PropKit.get("driver"));
	}
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("url"), PropKit.get("username"), PropKit.get("password").trim(),PropKit.get("driver"));
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
		
		DruidPlugin druidPlugin=createDruidPlugin();
		druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		druidPlugin.addFilter(wall);
		me.add(druidPlugin);
//		C3p0Plugin c3p0Plugin = createC3p0Plugin();
//		me.add(c3p0Plugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(true);
		me.add(arp);
		
		SchedulerPlugin sp = new SchedulerPlugin("job.properties");
		me.add(sp);

		// 所有配置在 MappingKit 中搞定
		_MappingKit.mapping(arp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	    me.add(new GlobalInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
	}
	
	public void afterJFinalStart() {
		super.afterJFinalStart();
		OreillyCos.setFileRenamePolicy(new RandomFileRenamePolicy());
	}
	public static void main(String[] args) {
		JFinal.start("WebContent", 8080, "/jfinal", 5);
	}
}
