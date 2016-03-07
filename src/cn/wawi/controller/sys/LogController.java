package cn.wawi.controller.sys;

import java.util.LinkedHashMap;
import java.util.List;
import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.JxlRender;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Log;
import cn.wawi.utils.ExcelFilter;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/sys_log")
public class LogController extends BaseController<Log>{
	    //String[] headers = new String[] {"id","用户","时间","描述","是否成功!","ip地址","系统","浏览器","请求参数","请求地址"};
		//String[] columns = new String[] {"id","optUser","inputTime","description","isSuccess","ip","os","browser","requestParam","operationCode"};
		
	    @Permission("sys:log:exportExcel")
		public void exportExcel(){
			List<Log> list = Log.dao.find(getSql());
			LinkedHashMap<String,String> fieldMap=new LinkedHashMap<String,String>();
			fieldMap.put("optUser", "用户名");
			fieldMap.put("realname", "姓名");
			fieldMap.put("ip", "ip地址");
			fieldMap.put("browser", "浏览器");
			fieldMap.put("os", "系统");
			fieldMap.put("isSuccess", "是否成功");
			fieldMap.put("operationCode", "请求地址");
			fieldMap.put("requestParam", "请求参数");
			fieldMap.put("inputTime", "时间");
			fieldMap.put("description", "描述");
			render(new JxlRender(list, fieldMap, "日志列表").setFilter(new ExcelFilter(){
				public Object filter(String name, Object value) {
					if("isSuccess".equals(name)){
						value=value.equals("1")?"成功!":"失败!";
					}
					return value;
				}
			}));
			//render(PoiRender.me(Log.dao.findAll()).fileName("ad.xls").headers(headers).cellWidth(5000).columns(columns).sheetName("日志列表"));
		}
}
