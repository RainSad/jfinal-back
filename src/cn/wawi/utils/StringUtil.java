package cn.wawi.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;


/**
 * @description 字符串工具类
 * @author 龚亮
 * @date 2014-10-15 15:11:24
 */
public class StringUtil extends StrKit{
	
	public static void main(String[] args) {
		System.out.println(StringUtil.class.getSimpleName());
		File file=new File("c:\\images\\"+"oLxrws5N-A1kPTkOLkeKr97c5oOw.png");
		System.out.println(file.exists());
	}
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> toMap(Model model) {
        Map<String, Object> map = Maps.newHashMap();
        Set<Entry<String, Object>> attrs = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Record toRecord(Model model) {
        Record record = new Record();
        Set<Entry<String, Object>> attrs = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            record.set(entry.getKey(), entry.getValue());
        }
        return record;
    }
    /**
    * 将Record转换成Map recordToMap
    * @param 参数说明
     * @return 返回对象
     * @Exception 异常对象
     */
    public static Map<String, Object> recordToMap(Record record) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (record != null) {
            String[] columns = record.getColumnNames();
            for (String col : columns) {
                map.put(col, record.get(col));
            }
        }
        return map;
    }
    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     * 
     * @param obj
     * @return
     */
    @SuppressWarnings({ "rawtypes"})
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String && (obj.equals(""))) {
            return true;
        } else if (obj instanceof Short && ((Short) obj).shortValue() == 0) {
            return true;
        } else if (obj instanceof Integer && ((Integer) obj).intValue() == 0) {
            return true;
        } else if (obj instanceof Double && ((Double) obj).doubleValue() == 0) {
            return true;
        } else if (obj instanceof Float && ((Float) obj).floatValue() == 0) {
            return true;
        } else if (obj instanceof Long && ((Long) obj).longValue() == 0) {
            return true;
        } else if (obj instanceof Boolean && !((Boolean) obj)) {
            return true;
        } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
            return true;
        }
        return false;
    }
    public static Model<?> toModel(Class<? extends Model<?>> clazz, Record record) {
        Model<?> model = null;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            return model;
        }
        for (String columnName : record.getColumnNames()) {
            model.set(columnName, record.get("columnName"));
        }
        return model;
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
	
	public static Integer parseInteger(String str){
		if(StrKit.isBlank(str)){
			return null;
		}
		return Integer.parseInt(str);
	}
	public static void getPicUrl(String str,String filename){
		FileOutputStream  fos=null;
		HttpURLConnection httpUrl = null;  
		BufferedInputStream  bis=null;
		try {
			URL url=new URL(str);
			httpUrl = (HttpURLConnection) url.openConnection();  
			bis = new BufferedInputStream(httpUrl.getInputStream());  
		    fos = new FileOutputStream("c:\\images\\"+filename+".png");  
			byte[] buf = new byte[1024];  
			int size = 0;  
			while ((size = bis.read(buf)) != -1) {  
				fos.write(buf, 0, size);  
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				 fos.flush(); 
				 fos.close();  
				 bis.close();  
				 httpUrl.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static boolean isInteger(String value){
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
