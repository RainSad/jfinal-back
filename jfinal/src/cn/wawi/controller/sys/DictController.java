package cn.wawi.controller.sys;


import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.JsonRender;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Dict;
import cn.wawi.utils.StringUtil;

@ControllerBind(controllerKey="/sys_dict")
public class DictController extends BaseController<Dict> {

	/*
	 * 字典列表
	 */
	public void list(){
		render(new JsonRender(findAllByPage()).forIE());
	}
	
	public String getSql(){
		StringBuilder sb=new StringBuilder("");
		sb.append("select * from sys_dict");
		if(StringUtil.notBlank(getPara("name"))){
			sb.append(" where (");
			sb.append(" value like ? ");
			sb.append(" OR label like ? ");
			sb.append(" OR type like ? ");
			sb.append(" OR description like ? ");
			String name=getPara("name");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			setParams("%"+name+"%");
			sb.append(" ) ");
		}
		buildSql(sb);
		return sb.toString();
	}
}
