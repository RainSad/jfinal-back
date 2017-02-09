package cn.wawi.common.base;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.render.Render;

public class KaptchaRender extends Render{
	
	protected static final Log LOG = Log.getLog(KaptchaRender.class);
	
	public static final String KAPTCHA="kaptcha";
	private Properties props;
	private Producer kaptchaProducer;
	private String sessionKeyValue;
	private String sessionKeyDateValue;

	public KaptchaRender() {
		this.props = new Properties();
        this.props.setProperty("kaptcha.textproducer.char.length", "4");
        this.props.setProperty("kaptcha.textproducer.font.size", "80");
        this.props.setProperty("kaptcha.image.width", "250");
        this.props.setProperty("kaptcha.image.height", "90");
        this.props.setProperty("kaptcha.border.color", "105,179,90");
        this.props.setProperty("kaptcha.border", "no");
        this.props.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.sessionKeyValue = KAPTCHA;
		this.sessionKeyDateValue = config.getSessionDate();
		
		
	}

	@Override
	public void render() {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache");
			response.setContentType("image/jpeg");
			String capText = this.kaptchaProducer.createText();
			request.getSession().setAttribute(this.sessionKeyValue, capText);
			request.getSession().setAttribute(this.sessionKeyDateValue, new Date());
			BufferedImage bi = this.kaptchaProducer.createImage(capText);
			ServletOutputStream out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean validate(Controller c, String field) {
		String userInputCaptcha=c.getPara(field);
		if (StrKit.isBlank(userInputCaptcha)) {
			return false;
		}
		userInputCaptcha = userInputCaptcha.toUpperCase();
		String kaptcha=(String) c.getSession().getAttribute("kaptcha");
		boolean result = userInputCaptcha.equalsIgnoreCase(kaptcha);
/*		if (result) {
			c.getSession().removeAttribute("kaptcha");
		}*/
		return result;
	}
}
