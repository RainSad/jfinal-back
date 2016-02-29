package cn.wawi.utils;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.kit.PathKit;
import jodd.util.SystemUtil;

public class SystemUtils extends SystemUtil{

	public static void main(String[] args) {
		System.out.println(PathKit.getWebRootPath());
		System.out.println(PathKit.getWebRootPath());
	}
	/**
	 * 获取项目名
	 */
	public static String getRootPath(){
		String str=System.getProperty("user.dir");
		return "/"+str.substring(str.lastIndexOf("\\")+1);
	}
	
	/**
	 * 获取客户端IP
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}
}
