package cn.wawi.common.weixin;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import cn.wawi.model.data.WeixinUser;
import cn.wawi.utils.StringUtil;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InShakearoundUserShakeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifyFailEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifySuccessEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

@Clear
@ControllerBind(controllerKey = "/msg")
public class WeixinMsgController extends MsgControllerAdapter {

	static Log logger = Log.getLog(WeixinMsgController.class);

	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		if(StrKit.isBlank(getPara("id"))){
			ac.setToken(PropKit.get("shequ.token"));
			ac.setAppId(PropKit.get("shequ.appId"));
			ac.setAppSecret(PropKit.get("shequ.AppSecret"));
		}else{
			ac.setToken(PropKit.get("wawi.token"));
			ac.setAppId(PropKit.get("wawi.appId"));
			ac.setAppSecret(PropKit.get("wawi.AppSecret"));
		}
		
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
	 * 实现父类抽方法，处理自定义菜单事件
	 */
	protected void processInMenuEvent(InMenuEvent inMenuEvent)
	{
		logger.debug("菜单事件：" + inMenuEvent.getFromUserName());
        if(inMenuEvent.getEventKey().equals("故障报修")){
			OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
			outMsg.setContent("如果您出现网络问题，请先检查您的错误代码。691表示密码错误或欠费。769表示您的网卡未启用，如果出现678、651、720、800错误,您可以拨打0851-86787072，稍后有将安排工作人员上门为您处理。");
			render(outMsg);
		}else if(inMenuEvent.getEventKey().equals("我的二维码")){
			WeixinUser user=WeixinUser.dao.findFirst("select * from k_weixin_user where weixin_code=?",inMenuEvent.getFromUserName());
			String weixinId=inMenuEvent.getFromUserName();
			OutImageMsg imgMsg=new OutImageMsg(inMenuEvent);
			try {
				if(user!=null&&StrKit.isBlank(user.getTicket())){
					String str = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+weixinId+"\"}}}";
			        ApiResult apiResult = QrcodeApi.create(str);
					user.setTicket(URLEncoder.encode(apiResult.getStr("ticket"), "utf-8"));
					user.update();
				}
				String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+user.getTicket();
				File file=new File("c:\\images\\"+weixinId+".png");
				 if(!file.exists()){
					StringUtil.getPicUrl(url, weixinId);
				 }
				 HttpRequest httpRequest = HttpRequest
					        .post("https://api.weixin.qq.com/cgi-bin/media/upload").form("access_token",AccessTokenApi.getAccessTokenStr(),"type","image","file", new File("c:\\images\\"+weixinId+".png"));
				 HttpResponse httpResponse = httpRequest.send();
				 JSONObject json=new JSONObject(httpResponse.bodyText());
				 imgMsg.setMediaId(json.getString("media_id"));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			HttpKit.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+AccessTokenApi.getAccessTokenStr(),"{\"touser\":\""+""+weixinId+""+"\",\"msgtype\":\"text\",\"text\":{\"content\":\"正在生成二维码,推广二维码即有机会赢取话费。\"}}");
			render(imgMsg);
		}else{
			OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
			outMsg.setContent(inMenuEvent.getEventKey());
			render(outMsg);
		}
	}
	/**
	 * 实现父类抽方法，处理文本消息
	 */
	protected void processInTextMsg(InTextMsg inTextMsg)
	{
		String msgContent = inTextMsg.getContent().trim();
		System.out.println("接受:"+msgContent);
		renderNull();
	}
	/**
	 * 实现父类抽方法，处理语音消息
	 */
	@Override
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inVoiceMsg);
		render(outCustomMsg);
	}
	/**
	 * 实现父类抽方法，处理视频消息
	 */
	@Override
	protected void processInVideoMsg(InVideoMsg inVideoMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inVideoMsg);
		render(outCustomMsg);
	}
	/**
	 * 实现父类抽方法，处理短视频消息
	 */
	@Override
	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inShortVideoMsg);
		render(outCustomMsg);
	}
	/**
	 * 实现父类抽方法，处理地址位置消息
	 */
	@Override
	protected void processInLocationMsg(InLocationMsg inLocationMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inLocationMsg);
		render(outCustomMsg);
	}
	/**
	 * 实现父类抽方法，处理链接消息
	 * 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
	 */
	@Override
	protected void processInLinkMsg(InLinkMsg inLinkMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inLinkMsg);
		render(outCustomMsg);
	}

	@Override
	protected void processInCustomEvent(InCustomEvent inCustomEvent)
	{
		logger.debug("测试方法：processInCustomEvent()");
		renderNull();
	}
	/**
	 * 实现父类抽方法，处理图片消息
	 */
	protected void processInImageMsg(InImageMsg inImageMsg)
	{
		//转发给多客服PC客户端
		OutCustomMsg outCustomMsg = new OutCustomMsg(inImageMsg);
		render(outCustomMsg);
	}

	/**
	 * 实现父类抽方法，处理关注/取消关注消息
	 */
	protected void processInFollowEvent(InFollowEvent inFollowEvent)
	{
		if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent.getEvent()))
		{
			logger.debug("关注：" + inFollowEvent.getFromUserName());
			WeixinUser user=WeixinUser.dao.findFirst("select * from k_weixin_user where weixin_code=?",inFollowEvent.getFromUserName());
			if(null==user){
				user=new WeixinUser();
				user.setWeixinCode(inFollowEvent.getFromUserName());
				user.save();
			}
			OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
			outMsg.setContent("关注微信号,获取更全面的宽带服务，通过二维码分享好友就有机会获得话费。");
			render(outMsg);
		}
		// 如果为取消关注事件，将无法接收到传回的信息
		if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent()))
		{
			logger.debug("取消关注：" + inFollowEvent.getFromUserName());
			renderNull();
		}
	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent)
	{
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent()))
		{
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
			WeixinUser user=WeixinUser.dao.findFirst("select * from k_weixin_user where weixin_code=?",inQrCodeEvent.getFromUserName());
			String qRSence=inQrCodeEvent.getEventKey().substring(8);
			if(user==null){
				user=new WeixinUser();
				user.setWeixinCode(inQrCodeEvent.getFromUserName());
				if(!StringUtil.isInteger(qRSence)){
					user.setFromWeixinCode(qRSence);
				}else{
					user.setResidenceId(StringUtil.parseInteger(qRSence));
				}
				user.save();
			}else{
				if(!StringUtil.isInteger(qRSence)){
					user.setFromWeixinCode(qRSence);
				}else{
					user.setResidenceId(StringUtil.parseInteger(qRSence));
				}
				user.update();
			}
			outMsg.setContent("关注微信号,获取更全面的宽带服务，通过二维码分享好友就有机会获得话费。");
			render(outMsg);
		}
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent()))
		{
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
		}
	}

	@Override
	protected void processInLocationEvent(InLocationEvent inLocationEvent)
	{
		logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
		outMsg.setContent("");
		render(outMsg);
	}

	@Override
	protected void processInMassEvent(InMassEvent inMassEvent)
	{
		logger.debug("测试方法：processInMassEvent()");
		renderNull();
	}

	@Override
	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults)
	{
		logger.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
		outMsg.setContent("语音识别内容是：" + inSpeechRecognitionResults.getRecognition());
		render(outMsg);
	}

	@Override
	protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent)
	{
		logger.debug("测试方法：processInTemplateMsgEvent()");
		renderNull();
	}
	@Override
	protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {
		logger.debug("摇一摇周边设备信息通知事件：" + inShakearoundUserShakeEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inShakearoundUserShakeEvent);
		outMsg.setContent("摇一摇周边设备信息通知事件UUID：" + inShakearoundUserShakeEvent.getUuid());
		render(outMsg);
	}

	@Override
	protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent) {
		logger.debug("资质认证成功通知事件：" + inVerifySuccessEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inVerifySuccessEvent);
		outMsg.setContent("资质认证成功通知事件：" + inVerifySuccessEvent.getExpiredTime());
		render(outMsg);
	}

	@Override
	protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent){
		logger.debug("资质认证失败通知事件：" + inVerifyFailEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inVerifyFailEvent);
		outMsg.setContent("资质认证失败通知事件：" + inVerifyFailEvent.getFailReason());
		render(outMsg);
	}

}






