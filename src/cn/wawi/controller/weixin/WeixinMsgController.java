package cn.wawi.controller.weixin;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
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
import com.jfinal.weixin.sdk.msg.out.OutMusicMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

/**
 * 将此 DemoController 在YourJFinalConfig 中注册路由，
 * 并设置好weixin开发者中心的 URL 与 token ，使 URL 指向该
 * DemoController 继承自父类 WeixinController 的 index
 * 方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
 */
@Clear
@ControllerBind(controllerKey = "/msg")
public class WeixinMsgController extends MsgControllerAdapter {

	static Log logger = Log.getLog(WeixinMsgController.class);
	private static final String helpStr = "\t发送 help 可获得帮助，发送\"视频\" 可获取视频教程，发送 \"美女\" 可看美女，发送 music 可听音乐 ，发送新闻可看JFinal新版本消息。公众号功能持续完善中";

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
	 * 实现父类抽方法，处理自定义菜单事件
	 */
	protected void processInMenuEvent(InMenuEvent inMenuEvent)
	{
		logger.debug("菜单事件：" + inMenuEvent.getFromUserName());
		if(inMenuEvent.getEventKey().equals("家长指南")){
			OutNewsMsg outMsg = new OutNewsMsg(inMenuEvent);
			outMsg.addNews(
					"中学的重点不在成绩，在于阅读",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FBUFkia3OGNzo9C1CicMOibQGalIUrUuZhmaL9u0r4CRn33vU71pY0GRRnzsEB3oeic9fXOgKrPRNIkWg/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4OTU2Mjk5OA==&mid=401348298&idx=2&sn=1f7da39327db6a8c9a1a874151466a77&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"寒假致家长: 无用方为大用, 请慢陪成长",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FDuQRBE3Y5ghtKAbYpSBavwelp14gUKKojgIJ5zvrlhZicSL2wz1oAwfc9PvHuhJORbgKtpLQSJF4g/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MjM5OTU2NDAzNA==&mid=404107369&idx=3&sn=aa64e146e953e594dc92f4322fb780b9&3rd=MzA3MDU4NTYzMw==&scene=6#rd");

			outMsg.addNews(
					"高情商的孩子更易成功，5招告诉你如何培养！",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FBUFkia3OGNzo9C1CicMOibQGalIUrUuZhmaL9u0r4CRn33vU71pY0GRRnzsEB3oeic9fXOgKrPRNIkWg/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MjM5Nzc3ODYxMg==&mid=402591967&idx=3&sn=111931645eeaea2f4c513248e03b501f&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"一位重点中学班主任逆谈家庭教育，很深刻！",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FDuQRBE3Y5ghtKAbYpSBavwelp14gUKKojgIJ5zvrlhZicSL2wz1oAwfc9PvHuhJORbgKtpLQSJF4g/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MjM5ODUzOTM3MQ==&mid=421553638&idx=2&sn=0cf8c8da97daf59392fbde43a84f2e40&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"一日三餐中的家庭教育",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FBUFkia3OGNzo9C1CicMOibQGalIUrUuZhmaL9u0r4CRn33vU71pY0GRRnzsEB3oeic9fXOgKrPRNIkWg/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MjM5MTAwMDIwOA==&mid=401669335&idx=3&sn=93dc78e5c01b8c2a8f16cf0e2543b305&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			render(outMsg);
		}else if(inMenuEvent.getEventKey().equals("everydayRecommend")){
			OutNewsMsg outMsg = new OutNewsMsg(inMenuEvent);
			outMsg.addNews(
					"初中数学三角函数知识点及常考题目类型总结",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FDuQRBE3Y5ghtKAbYpSBavwelp14gUKKojgIJ5zvrlhZicSL2wz1oAwfc9PvHuhJORbgKtpLQSJF4g/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=401354384&idx=1&sn=270f14c6bda5d720ece9b07953e17565#rd");
			outMsg.addNews(
					"中考物理光的反射知识点总结",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MjM5NTQ4MjM4MA==&mid=402071331&idx=3&sn=54270e2ba02e640b27fee4fb7952261b&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"初中数学三角函数知识点及常考题目类型总结",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MzA4NzI0NDEwOA==&mid=402154392&idx=8&sn=1c33e4fd71b838586c60f6d555d8a4ea&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"初中语文1-6册文言文知识点归纳",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MzAwMjM1MzMzOA==&mid=401329808&idx=5&sn=61052f94b775b8bcbbecda5072a13854&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			render(outMsg);
		}else if(inMenuEvent.getEventKey().equals("往期回顾")){
			OutNewsMsg outMsg = new OutNewsMsg(inMenuEvent);
			outMsg.addNews(
					"中学的重点不在成绩，在于阅读",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FBUFkia3OGNzo9C1CicMOibQGalIUrUuZhmaL9u0r4CRn33vU71pY0GRRnzsEB3oeic9fXOgKrPRNIkWg/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzA4OTU2Mjk5OA==&mid=401348298&idx=2&sn=1f7da39327db6a8c9a1a874151466a77&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"寒假致家长: 无用方为大用, 请慢陪成长",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FDuQRBE3Y5ghtKAbYpSBavwelp14gUKKojgIJ5zvrlhZicSL2wz1oAwfc9PvHuhJORbgKtpLQSJF4g/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MjM5OTU2NDAzNA==&mid=404107369&idx=3&sn=aa64e146e953e594dc92f4322fb780b9&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"初中数学三角函数知识点及常考题目类型总结",
					"湖北万维科技发展有限责任公司",
					"https://mmbiz.qlogo.cn/mmbiz/7zthsIrM3FDuQRBE3Y5ghtKAbYpSBavwelp14gUKKojgIJ5zvrlhZicSL2wz1oAwfc9PvHuhJORbgKtpLQSJF4g/0?wx_fmt=jpeg",
					"http://mp.weixin.qq.com/s?__biz=MzAxMzI5NDU5OA==&mid=401354384&idx=1&sn=270f14c6bda5d720ece9b07953e17565#rd");
			outMsg.addNews(
					"中考物理光的反射知识点总结",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MjM5NTQ4MjM4MA==&mid=402071331&idx=3&sn=54270e2ba02e640b27fee4fb7952261b&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"初中数学三角函数知识点及常考题目类型总结",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MzA4NzI0NDEwOA==&mid=402154392&idx=8&sn=1c33e4fd71b838586c60f6d555d8a4ea&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			outMsg.addNews(
					"初中语文1-6册文言文知识点归纳",
					"湖北万维科技发展有限责任公司",
					"",
					"http://mp.weixin.qq.com/s?__biz=MzAwMjM1MzMzOA==&mid=401329808&idx=5&sn=61052f94b775b8bcbbecda5072a13854&3rd=MzA3MDU4NTYzMw==&scene=6#rd");
			render(outMsg);
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
		// 帮助提示
		if(msgContent.contains("帮助")||StringUtils.containsIgnoreCase(msgContent, "help")){
			OutTextMsg outMsg = new OutTextMsg(inTextMsg);
			outMsg.setContent(helpStr);
			render(outMsg);
		}
		// 图文消息测试
		else if(msgContent.contains("新闻")||StringUtils.containsIgnoreCase(msgContent, "news")) {
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
			outMsg.addNews("湖北万维科技发展有限责任公司", "通信", "http://moriiy.wicp.net/weixin/images/wawi.jpg", "http://mp.weixin.qq.com/s?__biz=MzA4NDIxMzk4OQ==&mid=401102768&idx=1&sn=39ccc5d31fc14235cbe4a12c48d30fd9&scene=4#wechat_redirect");
			render(outMsg);
		}
		// 音乐消息测试
		else if (msgContent.contains("音乐") || StringUtils.containsIgnoreCase(msgContent, "music")) {
			OutMusicMsg outMsg = new OutMusicMsg(inTextMsg);
			outMsg.setTitle("When The Stars Go Blue-Venke Knutson");
			outMsg.setDescription("建议在 WIFI 环境下流畅欣赏此音乐");
			outMsg.setMusicUrl("http://www.jfinal.com/When_The_Stars_Go_Blue-Venke_Knutson.mp3");
			outMsg.setHqMusicUrl("http://www.jfinal.com/When_The_Stars_Go_Blue-Venke_Knutson.mp3");
			outMsg.setFuncFlag(true);
			render(outMsg);
		}
		else if (msgContent.contains("学习")||msgContent.contains("教程")) {
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
			outMsg.addNews(
					"湖北万维科技发展有限责任公司",
					"湖北万维科技发展有限责任公司",
					"http://moriiy.wicp.net/weixin/images/wawi.jpg",
					"http://mp.weixin.qq.com/s?__biz=MzA4NDIxMzk4OQ==&mid=401102768&idx=1&sn=39ccc5d31fc14235cbe4a12c48d30fd9&scene=4#wechat_redirect");

			render(outMsg);
		}
		else if (msgContent.contains("招聘")) {
			OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
			outMsg.addNews(
					"湖北万维科技发展有限责任公司(招聘)",
					"湖北万维科技发展有限责任公司(招聘)",
					"http://moriiy.wicp.net/weixin/images/wawi.jpg",
					"http://www.telecomhr.com/wx/");

			render(outMsg);
		}
		// 其它文本消息直接返回原值 + 帮助提示
		else {
			renderOutTextMsg("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n" + helpStr);
		}
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
			OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
			outMsg.setContent("您好，欢迎关注湖北万维科技发展有限责任公司!我们将提供通信相关服务,除法定节假日外,小编周一至周五均会在线。有问题欢迎联系我们！");
			render(outMsg);
		}
		// 如果为取消关注事件，将无法接收到传回的信息
		if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent()))
		{
			logger.debug("取消关注：" + inFollowEvent.getFromUserName());
		}
	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent)
	{
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent()))
		{
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
			outMsg.setContent("感谢您的关注，二维码内容：" + inQrCodeEvent.getEventKey());
			render(outMsg);
		}
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent()))
		{
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
		}
	}

	@Override
	protected void processInLocationEvent(InLocationEvent inLocationEvent)
	{
		logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
		outMsg.setContent("地理位置是：" + inLocationEvent.getLatitude());
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






