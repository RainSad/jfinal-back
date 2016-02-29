package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_department")
public class Department extends ModelExt<Department>{
	
	private static final long serialVersionUID = 1L;
	public static final Department dao=new Department();
}
