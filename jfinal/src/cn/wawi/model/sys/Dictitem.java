package cn.wawi.model.sys;

import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.plugin.tablebind.TableBind;

@TableBind(tableName="sys_dictitem")
public class Dictitem extends ModelExt<Dictitem>{

	private static final long serialVersionUID = 1L;
	public static final Dictitem dao=new Dictitem();
}
