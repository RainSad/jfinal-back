package open.weixin.utils.menu;

import com.jfinal.kit.JsonKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;
/**
 * 菜单管理器类 
 * @author mini
 * 2016年5月30日
 */
public class MenuManager  {
	 public static void main(String[] args) { 
		 
		   String jsonMenu = JsonKit.toJson(getTestMenu()).toString();
		   System.out.println(jsonMenu);
		   ApiConfig ac = new ApiConfig();
			
			// 配置微信 API 相关常量
			ac.setAppId("*******");
			ac.setAppSecret("*******");
		    ApiConfigKit.setThreadLocalApiConfig(ac);
		   
		   //创建菜单
	       ApiResult apiResult=MenuApi.createMenu(jsonMenu);
	       System.out.println(apiResult.getJson());
	 }  
	 
	 
	  
	    /** 
	     * 组装菜单数据 
	     * @return 
	     */  
	    private static Menu getTestMenu() {  
	    	ClickButton btn11 = new ClickButton();  
	        btn11.setName("停气公告");  
	        btn11.setType("click");  
	        btn11.setKey("menu_1_1");
	  
	        ClickButton btn12 = new ClickButton();  
	        btn12.setName("安检公告");  
	        btn12.setType("click");  
	        btn12.setKey("menu_1_2");  
	  
	        ClickButton btn13 = new ClickButton();  
	        btn13.setName("业务指南");  
	        btn13.setType("click");  
	        btn13.setKey("menu_1_3");
	        
	        ClickButton btn14 = new ClickButton();  
	        btn14.setName("营业网点");  
	        btn14.setType("click");  
	        btn14.setKey("menu_1_4");
	  
	        ViewButton btn21 = new ViewButton();  
	        btn21.setName("我要缴费");  
	        btn21.setType("view");  
	        btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2flanyan.hbwwcc.com%2flanyan%2fwx_oauth%2fuser_pay&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

	  
	        ViewButton btn22 = new ViewButton();  
	        btn22.setName("卡号绑定");  
	        btn22.setType("view");  
	        btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2flanyan.hbwwcc.com%2flanyan%2fwx_oauth%2fcard_bind&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

	        ViewButton btn23 = new ViewButton();  
	        btn23.setName("缴费记录");  
	        btn23.setType("view");  
	        btn23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2flanyan.hbwwcc.com%2flanyan%2fwx_oauth%2fuser_pay_history&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

	        
	        ClickButton btn31 = new ClickButton();  
	        btn31.setName("个人中心");  
	        btn31.setType("click");  
	        btn31.setKey("menu_3_1"); 
	  
	        
	        ClickButton btn32 = new ClickButton();  
	        btn32.setName("增值服务");  
	        btn32.setType("click");  
	        btn32.setKey("menu_3_2"); 
	  
	        //http://tencent://message/?uin=572839485&Site=在线咨询&Menu=yes
	        //http://wpa.qq.com/msgrd?v=3&uin=572839485&site=qq&menu=yes
	        
	        ViewButton btn33 = new ViewButton();  
	        btn33.setName("在线咨询");  
	        btn33.setType("view");  
	        btn33.setUrl("http://wpa.qq.com/msgrd?v=3&uin=411400580&site=qq&menu=yes");  
	  
	        
	        ComButton mainBtn1 = new ComButton();  
	        mainBtn1.setName("信息公告");  
	        mainBtn1.setSub_button(new Button[] {btn11,btn12, btn13,btn14});  
	  
	        ComButton mainBtn2 = new ComButton();  
	        mainBtn2.setName("燃气管家");  
	        mainBtn2.setSub_button(new Button[] { btn21, btn22 ,btn23});  
	  
	        ComButton mainBtn3 = new ComButton();  
	        mainBtn3.setName("我的");  
	        mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33});  
	  
	        /** 
	         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
	         *  
	         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
	         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
	         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
	         */  
	        Menu menu = new Menu();  
	        menu.setButton(new Button[] { mainBtn1, mainBtn2, btn32 });  
	        return menu;  
	    }
}
