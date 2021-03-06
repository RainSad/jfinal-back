package open.weixin.controller;

import org.json.JSONException;
import org.json.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
/**
 * @author mini
 * 2015年12月5日下午2:20:44
 *
 */
@Clear
@ControllerBind(controllerKey = "/wx_oauth")
public class WeiXinOauthController extends ApiController{
	static Log log = Log.getLog(WeiXinOauthController.class);
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
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
	@SuppressWarnings("all")
	public void index() throws JSONException {
		int  subscribe=0;
		//用户同意授权，获取code
		String code=getPara("code");
		String state=getPara("state");
		if (code!=null) {
			String appId=ApiConfigKit.getApiConfig().getAppId();
			String secret=ApiConfigKit.getApiConfig().getAppSecret();
			//通过code换取网页授权access_token
			SnsAccessToken snsAccessToken=SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
//			String json=snsAccessToken.getJson();
			String token=snsAccessToken.getAccessToken();
			String openId=snsAccessToken.getOpenid();
			//拉取用户信息(需scope为 snsapi_userinfo)
			ApiResult apiResult=SnsApi.getUserInfo(token, openId);
			
			log.warn("getUserInfo:"+apiResult.getJson());
			if (apiResult.isSucceed()) {
				JSONObject jsonObject=new JSONObject(apiResult.getJson());
				System.out.println(jsonObject.toString());
				String nickName=jsonObject.getString("nickname");
				//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
				int sex=jsonObject.getInt("sex");
				String city=jsonObject.getString("city");//城市
				String province=jsonObject.getString("province");//省份
				String country=jsonObject.getString("country");//国家
				String headimgurl=jsonObject.getString("headimgurl");
				//String unionid=jsonObject.getString("unionid");
				//获取用户信息判断是否关注
				ApiResult userInfo = UserApi.getUserInfo(openId);
				log.warn(JsonKit.toJson("is subsribe>>"+userInfo));
				if (userInfo.isSucceed()) {
					String userStr = userInfo.toString();
					subscribe=new JSONObject(userStr).getInt("subscribe");
				}
			}
			
         renderFreeMarker("/weixin/lanyan/user-binding.html");			
		}
	}
}
