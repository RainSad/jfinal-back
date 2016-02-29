package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_dict")
public class Dict extends ModelExt<Dict>{
	
	private static final long serialVersionUID = 1L;
	public static final Dict dao=new Dict();
}
