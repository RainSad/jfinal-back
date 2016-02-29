package cn.wawi.controller.weixin;

import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.render.JsonRender;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.jfinal.ApiController;

@Clear
@ControllerBind(controllerKey = "/api")
public class WeixinApiController extends ApiController {
	
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("weixin.token"));
		ac.setAppId(PropKit.get("weixin.appId"));
		ac.setAppSecret(PropKit.get("weixin.AppSecret"));
		
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
		//String str="{\"button\":[{\"name\":\"通信精彩\",\"sub_button\":[{\"type\":\"view\",\"name\":\"业界精彩文章\",\"url\":\"http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzAxMzI5NDU5OA==#wechat_webview_type=1&wechat_redirect\"},{\"type\":\"click\",\"name\":\"每日推荐\",\"key\":\"everydayRecommend\"},{\"type\":\"click\",\"name\":\"LTE教程\",\"key\":\"LTE全套培训教程赠送，下载链接：http://pan.baidu.com/s/1gdzRYEn 密码：75ti （如果提示下载不了或资源不存在，请登录或注册一个百度帐号即可），资源会不断更新，敬请关注哦\"}]},{\"name\":\"交流分享\",\"sub_button\":[{\"type\":\"view\",\"name\":\"通信社区\",\"url\":\"http://buluo.qq.com/mobile/barindex.html?_lv=34778&bid=12821&_bid=128&_wv=1027&from=share_link#type=&target=hot&scene=search&time_redirect=1449192712559&webview=1\"},{\"type\":\"view\",\"name\":\"专家解答\",\"url\":\"http://moriiy.wicp.net/weixin/weixin_problem/main\"},{\"type\":\"click\",\"name\":\"知识库\",\"key\":\"未完成,后续将完成知识库.....\"},{\"type\":\"click\",\"name\":\"联系我们\",\"key\":\"投稿邮箱 gongliang@hbwawi.net\n 电话: 027-478539 \n 邮箱:gongliang@hbwawi.net\"}]},{\"name\":\"招聘职位\",\"sub_button\":[{\"type\":\"view\",\"name\":\"最新招聘\",\"url\":\"http://www.telecomhr.com/wx/\"},{\"type\":\"click\",\"name\":\"每日推荐\",\"key\":\"每日推荐。\"}]}]}";
		String str="{\"button\":[{\"name\":\"家长指南\",\"type\":\"click\",\"key\":\"家长指南\"},{\"name\":\"学习\",\"sub_button\":[{\"type\":\"click\",\"name\":\"学习题库\",\"key\":\"everydayRecommend\"},{\"type\":\"click\",\"name\":\"心里辅导\",\"key\":\"xinli\"},{\"type\":\"click\",\"name\":\"模拟试卷\",\"key\":\"模拟试卷\"},{\"type\":\"click\",\"name\":\"往期回顾\",\"key\":\"往期回顾\"},{\"type\":\"view\",\"name\":\"名师一新\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=401354274&idx=1&sn=1549dfe71ad22e3359a9ecc03c91d2f3#rd\"}]},{\"name\":\"个人中心\",\"sub_button\":[{\"type\":\"click\",\"name\":\"个人信息\",\"key\":\"个人信息\"},{\"type\":\"click\",\"name\":\"个人问答\",\"key\":\"个人问答\"},{\"type\":\"click\",\"name\":\"个人推广\",\"key\":\"个人推广\"},{\"type\":\"view\",\"name\":\"积分管理\",\"url\":\"http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzAxMzI5NDU5OA==#wechat_webview_type=1&wechat_redirect\"}]}]}";
		ApiResult apiResult = MenuApi.createMenu(str);
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	/**
	 * 创建素材
	 */
	public void createMateria(){
		String str="{\"articles\":[{\"title\":\"看这个通信屌丝如何用大数据追到女神？\",\"thumb_media_id\":\"akjkld1323\",\"author\":\"湖北万维科技发展有限责任公司\",\"digest\":\"屌丝如何用大数据追到女神\",\"show_cover_pic\":1,\"content\":\"小柯25岁，通讯单身屌丝男一枚，热衷大数据，并决定认真钻研，用数据分析来实现自己的“脱单计划”。\",\"content_source_url\":\"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=400665418&idx=3&sn=a8342d2094543fa6f15b318315082b6f#rd\"}]}";
		String jsonResult = HttpKit.post("https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=" + AccessTokenApi.getAccessTokenStr(), str);
        render(new JsonRender(jsonResult).forIE());
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
		String str = " {\n" +
				"           \"touser\":\"ohbweuNYB_heu_buiBWZtwgi4xzU\",\n" +
				"           \"template_id\":\"9SIa8ph1403NEM3qk3z9-go-p4kBMeh-HGepQZVdA7w\",\n" +
				"           \"url\":\"http://www.sina.com\",\n" +
				"           \"topcolor\":\"#FF0000\",\n" +
				"           \"data\":{\n" +
				"                   \"first\": {\n" +
				"                       \"value\":\"恭喜你购买成功！\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"keyword1\":{\n" +
				"                       \"value\":\"去哪儿网发的酒店红包（1个）\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"keyword2\":{\n" +
				"                       \"value\":\"1元\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   },\n" +
				"                   \"remark\":{\n" +
				"                       \"value\":\"欢迎再次购买！\",\n" +
				"                       \"color\":\"#173177\"\n" +
				"                   }\n" +
				"           }\n" +
				"       }";
		ApiResult apiResult = TemplateMsgApi.send(str);
		renderText(apiResult.getJson());
	}

	/**
	 * 获取参数二维码
	 */
	public void getQrcode()
	{
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
		ApiResult apiResult = QrcodeApi.create(str);
		renderText(apiResult.getJson());

//        String str = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"123\"}}}";
//        ApiResult apiResult = QrcodeApi.create(str);
//        renderText(apiResult.getJson());
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
}

