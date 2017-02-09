package open.weixin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.render.JsonRender;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import cn.wawi.common.base.R;
import cn.wawi.utils.DateUtil;

/**
 * 感谢 *半杯* 童鞋联调支付API
 * @author L.cm
 */
@Clear
@ControllerBind(controllerKey = "/wx_pay")
public class WeixinPayController extends Controller {
	static Log logger = Log.getLog(WeixinPayController.class);
	//商户相关资料
	private static String appid =PropKit.get("appId");
	private static String partner = PropKit.get("partner");
	private static String paternerKey =PropKit.get("paternerKey");
	private static String notify_url = "http://lanyan.hbwwcc.com/lanyan/weixin/lanyan/notify.html";
	
	/**
	 * 统一下单以及支付
	 */
	public void pay(){
		R<Map<String, String>> r=new R<Map<String, String>>("下单成功!");
		String openId = getPara("openId");
        String total_fee=getParaToInt("pay_amount")*100+"";
        total_fee="1";
        String pay_busisn=getPara("pay_busisn");
        if(StrKit.isBlank(pay_busisn)){
    		if("2".equals(getPara("user_type"))){
                pay_busisn="WX_IC_"+DateUtil.getDate("yyyyMMddHHmmssS")+((int)(Math.random()*900)+100);
    		}else if("3".equals(getPara("user_type"))){
                pay_busisn="WX_MC_"+DateUtil.getDate("yyyyMMddHHmmssS")+((int)(Math.random()*900)+100);
    		}else{
                pay_busisn="WX_SC_"+DateUtil.getDate("yyyyMMddHHmmssS")+((int)(Math.random()*900)+100);
    		}
        }
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", partner);
		params.put("device_info", "WEB");
		params.put("body", "天然气缴费");
		params.put("out_trade_no", pay_busisn);
		params.put("total_fee", total_fee);
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		params.put("spbill_create_ip", ip);
		params.put("trade_type", TradeType.JSAPI.name());
		params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
		params.put("notify_url", notify_url);
		params.put("openid", openId);
		params.put("time_start", DateUtil.getDate("yyyyMMddHHmmss"));
		params.put("time_expire", DateUtil.formatDate(DateUtil.addHours(new Date(), 1), "yyyyMMddHHmmss"));
		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);
		String xmlResult = PaymentApi.pushOrder(params);
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		logger.debug("统一下单:"+result);
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			r.setResMsg(return_msg);
			r.setResCode(0);
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			r.setResMsg(return_msg);
			r.setResCode(0);
		}
        String prepay_id = result.get("prepay_id");
		
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", appid);
		packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		packageParams.put("nonceStr", System.currentTimeMillis() + "");
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		packageParams.put("paySign", packageSign);
		
		r.setExtra(packageParams);
		render(new JsonRender(r).forIE());
	}
	/**
	 * 微信支付
	 */
	public void wx_pay(){
		R<Map<String, String>> r=new R<Map<String, String>>("支付成功!");
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", appid);
		packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		packageParams.put("nonceStr", System.currentTimeMillis() + "");
		packageParams.put("package", "prepay_id=" + getPara("prepay_id"));
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		packageParams.put("paySign", packageSign);
		r.setExtra(packageParams);
		render(new JsonRender(r).forIE());
	}
	public void pay_notify() {
		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(getRequest());
		System.out.println("支付通知="+xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		
		String result_code  = params.get("result_code");
		// 总金额
		//String totalFee     = params.get("total_fee");
		// 商户订单号
		//String orderId      = params.get("out_trade_no");
		// 微信支付订单号
		//String transId      = params.get("transaction_id");
		// 支付完成时间，格式为yyyyMMddHHmmss
		//String timeEnd      = params.get("time_end");
		
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		
		if(PaymentKit.verifyNotify(params, paternerKey)){
			if (("SUCCESS").equals(result_code)) {
				//更新订单信息
				System.out.println("更新订单信息");
				
				Map<String, String> xml = new HashMap<String, String>();
				xml.put("return_code", "SUCCESS");
				xml.put("return_msg", "OK");
				renderText(PaymentKit.toXml(xml));
				return;
			}
		}
		renderText("");
	}
	public Map<String,String> getparamaterMap(){
		Map<String,String> params=Maps.newHashMap();
		Map<String,String[]> param=getParaMap();
		for(String key: param.keySet()){
			params.put(key, getPara(key));
		}
		return params;
	}
}
