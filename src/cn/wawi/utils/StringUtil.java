package cn.wawi.utils;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;


/**
 * @description 字符串工具类
 * @author 龚亮
 * @date 2014-10-15 15:11:24
 */
public class StringUtil extends StrKit{
	
	public static void main(String[] args) {
		System.out.println(StringUtil.class.getSimpleName());
	}

	/**
	* 截取文件后缀
	*/
	public static String subFileName(String fileName) {
	    int index = fileName.lastIndexOf(".");
		return -1==index?fileName:fileName.substring(index + 1);
	}
	
	/**
	 * 获取随机uuid文件名
	 */
	public static String generateRandonFileName() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获得hashcode生成二级目录
	 */
	public static String generateRandomDir(String uuidFileName) {
		int hashCode = uuidFileName.hashCode();
		// 一级目录
		int d1 = hashCode & 0xf;
		// 二级目录
		int d2 = (hashCode >> 4) & 0xf;
		return "/" + d1 + "/" + d2;
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
	public static Object[] toArray(List<Object> list){
		Object[] o=null;
		if(list!=null){
			o=new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				o[i]=list.get(i);
			}
		}
        return o;
	}
}
