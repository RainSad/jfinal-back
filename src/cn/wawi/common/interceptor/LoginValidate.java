package cn.wawi.common.interceptor;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import cn.wawi.common.base.KaptchaRender;

public class LoginValidate extends Validator{

	protected void handleError(Controller c) {
		c.keepPara("username");
		c.renderFreeMarker("/login.html");
	}

	protected void validate(Controller c) {
		validateRequired("username", "msg", "请输入您的登陆账号");
		
		validateRequired("password", "msg", "请输入您的密码");
		validateString("password", 3, 24, "msg", "请输入6~24位的密码");
		
		validateRequired("captcha", "msg", "请输入验证码");
		validateCaptcha("captcha", "msg", "验证码错误");
	}

	protected void validateCaptcha(String field, String errorKey, String errorMessage) {
		if (!KaptchaRender.validate(getController(), field))
			addError(errorKey, errorMessage);
	}
}
