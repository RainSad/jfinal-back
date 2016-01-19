package cn.wawi.common.interceptor;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
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

}
