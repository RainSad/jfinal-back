package open.weixin.controller;

import java.io.File;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import cn.wawi.utils.DateUtil;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.render.JsonRender;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.api.JsTicketApi.JsApiType;
import com.jfinal.weixin.sdk.jfinal.ApiController;

@Clear
@ControllerBind(controllerKey = "/wx_api")
public class WeixinApiController extends ApiController {
	
	static Log logger = Log.getLog(WeixinApiController.class);
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("AppSecret"));

		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	/**
	 * 获取公众号菜单
	 */
	public void getMenu() {
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	/**
	 * 创建菜单
	 */
	public void createMenu()
	{
		//String str="{\"button\":[{\"name\":\"我的宽带\",\"sub_button\":[{\"type\":\"view\",\"name\":\"宽带报装\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2fpzl.hbwwcc.com%2fweixin%2fweixin%2fkuandai.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"},{\"type\":\"click\",\"name\":\"故障报修\",\"key\":\"故障报修\"}]},{\"name\":\"会员中心\",\"sub_button\":[{\"type\":\"click\",\"name\":\"个人中心\",\"key\":\"个人中心\"},{\"type\":\"click\",\"name\":\"推广有奖\",\"key\":\"我的二维码\"}]}]}";
       // String str="{\"button\":[{\"name\":\"我的宽带\",\"sub_button\":[{\"type\":\"view\",\"name\":\"宽带报装\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2fpzl.hbwwcc.com%2fweixin%2fweixin%2fkuandai.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"},{\"type\":\"click\",\"name\":\"故障报修\",\"key\":\"故障报修\"},{\"type\":\"view\",\"name\":\"优惠活动\",\"url\":\"http://pzl.hbwwcc.com/weixin/weixin/youhui.html\"}]},{\"name\":\"会员中心\",\"sub_button\":[{\"type\":\"view\",\"name\":\"个人中心\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2fpzl.hbwwcc.com%2fweixin%2fweixin%2fperson.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"},{\"type\":\"click\",\"name\":\"推广有奖\",\"key\":\"我的二维码\"}]}]}";
		//String str="{\"button\":[{\"name\":\"我的宽带\",\"sub_button\":[{\"type\":\"view\",\"name\":\"宽带报装\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2fpzl.hbwwcc.com%2fweixin%2fweixin%2fkuandai.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"},{\"type\":\"click\",\"name\":\"故障报修\",\"key\":\"故障报修\"},{\"type\":\"view\",\"name\":\"优惠活动\",\"url\":\"http://pzl.hbwwcc.com/weixin/weixin/youhui.html\"}]},{\"name\":\"会员中心\",\"sub_button\":[{\"type\":\"view\",\"name\":\"个人中心\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d9d3839bcf7a60b&redirect_uri=http%3a%2f%2fpzl.hbwwcc.com%2fweixin%2fweixin%2fperson.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"}]},{\"name\":\"推广有礼\",\"sub_button\":[{\"type\":\"click\",\"name\":\"推广二维码\",\"key\":\"我的二维码\"}]}]}";
        String s="{\"button\":[{\"name\":\"资讯吧\",\"sub_button\":[{\"type\":\"click\",\"name\":\"历史回顾\",\"key\":\"历史回顾\"},{\"type\":\"click\",\"name\":\"精华原创\",\"key\":\"精华原创\"},{\"type\":\"click\",\"name\":\"跟我学\",\"key\":\"跟我学\"},{\"type\":\"click\",\"name\":\"赛事追踪\",\"key\":\"赛事追踪\"}]},{\"name\":\"活动吧\",\"sub_button\":[{\"type\":\"view\",\"name\":\"团教练\",\"url\":\"http://pzl.hbwwcc.com/oa/wx_pay/\"},{\"type\":\"view\",\"name\":\"约球友\",\"url\":\"http://pzl.hbwwcc.com/oa/wx_pay/\"},{\"type\":\"view\",\"name\":\"羽球投票\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=402286090&idx=1&sn=e0348df2c6d44feea6fee0e0c899310f#rd\"}]},{\"name\":\"商城吧\",\"sub_button\":[{\"type\":\"click\",\"name\":\"微商城\",\"key\":\"微商城\"}]}]}";
        ApiResult apiResult = MenuApi.createMenu(s);
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	/**
	 * 创建素材
	 */
	public void createMateria(){
		//String str="{\"articles\":[{\"title\":\"看这个通信屌丝如何用大数据追到女神？\",\"thumb_media_id\":\"akjkld1323\",\"author\":\"湖北万维科技发展有限责任公司\",\"digest\":\"屌丝如何用大数据追到女神\",\"show_cover_pic\":1,\"content\":\"小柯25岁，通讯单身屌丝男一枚，热衷大数据，并决定认真钻研，用数据分析来实现自己的“脱单计划”。\",\"content_source_url\":\"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=400665418&idx=3&sn=a8342d2094543fa6f15b318315082b6f#rd\"}]}";
		//String jsonResult = HttpKit.post("https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + AccessTokenApi.getAccessTokenStr()+"&type=image", str);
		   HttpRequest httpRequest = HttpRequest
			        .post("https://api.weixin.qq.com/cgi-bin/media/upload").form("access_token",AccessTokenApi.getAccessTokenStr(),"type","image","file", new File("d:\\5.jpg"));
		   HttpResponse httpResponse = httpRequest.send();
		   System.out.println(httpResponse.bodyText());
		render(new JsonRender(httpResponse).forIE());
	}
	/**
	 * 获取素材列表
	 */
	public void getMaterialList(){
		String str="{\"type\":\"news\",\"offset\":0,\"count\":10}";
		String jsonResult = HttpKit.post("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + AccessTokenApi.getAccessTokenStr(), str);
        render(new JsonRender(jsonResult).forIE());
	}
	/**
	 * 获取公众号关注用户
	 */
	public void getFollowers()
	{
		ApiResult apiResult = UserApi.getFollows();
		renderText(apiResult.getJson());
	}

	/**
	 * 获取用户信息
	 */
	public void getUserInfo()
	{
		ApiResult apiResult = UserApi.getUserInfo("ohbweuNYB_heu_buiBWZtwgi4xzU");
		renderText(apiResult.getJson());
	}
	/**
	 * 发送模板消息
	 */
	public void sendMsg()
	{
	}
	public void addBroadbandAfter()
	{
		String str="{\"touser\":\"oLxrwsxnrFZYCZoBqJi2b7wt115U\",\"template_id\":\"Oj519R1zHI05PMkSY44WEcwclB_8o59daEGPLR5AgRA\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"中国移动宽带\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"先生\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\""+DateUtil.getTime()+"\",\"color\":\"#173177\"},\"remark\":{\"value\":\"有新用户申请宽带办理,请及时处理。\",\"color\":\"#173177\"}}}";
		ApiResult apiResult = TemplateMsgApi.send(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取参数二维码
	 */
	public void getQrcode()
	{
//		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
//		ApiResult apiResult = QrcodeApi.create(str);
//		renderText(apiResult.getJson());

        String str = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+getPara("senceStr")+"\"}}}";
        ApiResult apiResult = QrcodeApi.create(str);
        renderText(apiResult.getJson());
	}

	/**
	 * 长链接转成短链接
	 */
	public void getShorturl()
	{
		String str = "{\"action\":\"long2short\"," +
				"\"long_url\":\"http://wap.koudaitong.com/v2/showcase/goods?alias=128wi9shh&spm=h56083&redirect_count=1\"}";
		ApiResult apiResult = ShorturlApi.getShorturl(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取客服聊天记录
	 */
	public void getRecord()
	{
		String str = "{\n" +
				"    \"endtime\" : 987654321,\n" +
				"    \"pageindex\" : 1,\n" +
				"    \"pagesize\" : 10,\n" +
				"    \"starttime\" : 123456789\n" +
				" }";
		ApiResult apiResult = CustomServiceApi.getRecord(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取微信服务器IP地址
	 */
	public void getCallbackIp()
	{
		ApiResult apiResult = CallbackIpApi.getCallbackIp();
		renderText(apiResult.getJson());
	}
	/*
	 * 微信js端config配置
	 */
	public void getWeixinJsConfig(){
        String url="http://moriiy.wicp.net/admin/weixin/test/test.html";
		String jsapi_ticket=JsTicketApi.getTicket(JsApiType.jsapi).getTicket();
		String timestamp=Long.toString(System.currentTimeMillis() / 1000);
		String noncestr=UUID.randomUUID().toString();
		String tempStr = new StringBuilder().append("jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url).toString();
		String signature = HashKit.sha1(tempStr);
		JSONObject  json=new JSONObject();
		try {
			json.put("timestamp", timestamp);
			json.put("nonceStr", noncestr);
			json.put("signature", signature);
			json.put("appId", ApiConfigKit.getApiConfig().getAppId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		render(new JsonRender(json.toString()));
	}
	public void getWeixinJsConfig1(){
        String url="http://moriiy.wicp.net/admin/weixin/test/weixin.html";
		String jsapi_ticket=JsTicketApi.getTicket(JsApiType.jsapi).getTicket();
		String timestamp=Long.toString(System.currentTimeMillis() / 1000);
		String noncestr=UUID.randomUUID().toString();
		String tempStr = new StringBuilder().append("jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url).toString();
		String signature = HashKit.sha1(tempStr);
		JSONObject  json=new JSONObject();
		try {
			json.put("timestamp", timestamp);
			json.put("nonceStr", noncestr);
			json.put("signature", signature);
			json.put("appId", ApiConfigKit.getApiConfig().getAppId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		render(new JsonRender(json.toString()));
	}
	/*
	 * 微信js端网页授权
	 */
	public void getOAuth(){
		String str=HttpKit.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ApiConfigKit.getApiConfig().getAppId()+"&secret="+ApiConfigKit.getApiConfig().getAppSecret()+"&code="+getPara("code")+"&grant_type=authorization_code");
	    String result="";
		try {
			JSONObject json=new JSONObject(str);
			result=HttpKit.get("https://api.weixin.qq.com/sns/userinfo?access_token="+json.getString("access_token")+"&openid="+json.getString("openid")+"&lang=zh_CN");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiResult apiResult=new ApiResult(result);
//		WeixinUser user=WeixinUser.dao.findFirst("select * from k_weixin_user where weixin_code=?",apiResult.get("openid"));
//		if(null!=user&&StrKit.isBlank(user.getNickName())){
//			user.setNickName(apiResult.getStr("nickname"));
//			user.update();
//		}
		renderText(apiResult.getJson());
	}
}

