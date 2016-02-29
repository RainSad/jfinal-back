package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_log")
public class Log extends ModelExt<Log>{
	
	private static final long serialVersionUID = 1L;
	public static final Log dao=new Log();
}
