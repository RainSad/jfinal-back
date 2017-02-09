package cn.wawi.utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;

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
		System.out.println(toUnicode("sae事件"));
		System.out.println(mul(2.53,100));
		System.out.println(div(92470,100));
		float f=253.0F;
		System.out.println((int)f);
		System.out.println(generateRandomDateDir("task/"));
		System.out.println(subFileName("d:\\fs\\upload\\jfinal\\ueditor\\image\\2017\\01\\19\\4.jpg"));
	}
	/**
	 * 计算乘法
	 */
    public static double mul( double  v1, double  v2) 
    { 
       BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
       BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
       return  b1.multiply(b2).doubleValue(); 
   } 
	/**
	 * 计算除法
	 */
    public  static  double div( double  v1, double  v2) 
    { 
        return  div(v1,v2,2); 
    } 
    public   static   double  div( double  v1, double  v2, int  scale) 
    { 
        if (scale<0) 
        { 
            throw   new  IllegalArgumentException("The scale must be a positive integer or zero"); 
       } 
       BigDecimal b1 =  new  BigDecimal(Double.toString(v1)); 
       BigDecimal b2 =  new  BigDecimal(Double.toString(v2)); 
        return  b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
   } 
	/**
	 * 计算百分比
	 */
	public static String accuracy(int num ,int total){
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();  
        //可以设置精确几位小数  
        df.setMaximumFractionDigits(1);  
        //模式 例如四舍五入  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        double accuracy_num = (double)num / (double)total * 100; 
        return df.format(accuracy_num)+"%";
	}
	/**
	* 中文转unicode
	*/
	public static String toUnicode(String str) {
       String result="";
       for (int i = 0; i < str.length(); i++) {
		   int c= str.charAt(i);
		   if(c>=19968&&c<=171941){
			   result+="//u" + Integer.toHexString(c);
		   }else{
			   result+=str.charAt(i);
		   }
	   }
       return result;
	}
	/**
	* 截取文件后缀
	*/
	public static String subFileName(String fileName) {
	    int index = fileName.lastIndexOf(".");
		return -1==index?fileName:fileName.substring(index + 1);
	}
	public static String subFileNamePrex(String fileName) {
	    int index = fileName.lastIndexOf(".");
		return -1==index?fileName:fileName.substring(0,index);
	}
	/**
	 * 获取随机时间文件名
	 */
	public static String getTimeFileName() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS");
	}
	/**
	 * 获取随机uuid文件名
	 */
	public static String getRandomFileName() {
		return UUID.randomUUID().toString();
	}

	/**
	 * prex 前缀目录
	 * 获得日期生成二级目录
	 */
	public static String generateRandomDateDir(String prex) {
		Date date=new Date();
		return prex+ DateFormatUtils.format(date,"yyyy") + "/" + DateFormatUtils.format(date,"MM") + "/" + DateFormatUtils.format(date,"dd") + "/";
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
}