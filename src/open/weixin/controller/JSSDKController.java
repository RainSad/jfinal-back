package open.weixin.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;

import cn.wawi.common.interceptor.GlobalInterceptor;
import open.weixin.interceptor.JSSDKInterceptor;

@Before(JSSDKInterceptor.class)
@Clear(GlobalInterceptor.class)
@ControllerBind(controllerKey="/wx_js")
public class JSSDKController extends Controller{
	
	public void index(){
		renderFreeMarker("/weixin/test/weixin.html");
	}
	
	public void pay(){
		renderFreeMarker("/weixin/test/pay.html");
	}
	
	public void pic(){
		renderFreeMarker("/weixin/test/pic.html");
	}
}
