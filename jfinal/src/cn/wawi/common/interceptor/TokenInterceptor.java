package cn.wawi.common.interceptor;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class TokenInterceptor extends Validator {

	@Override
	protected void handleError(Controller c) {
	}

	@Override
	protected void validate(Controller c) {
		 validateToken("update", "msg", "alert('上次已更新，请不要重复提交')");
		 validateToken("save", "msg", "alert('上次已保存，请不要重复提交')");
	}

}
