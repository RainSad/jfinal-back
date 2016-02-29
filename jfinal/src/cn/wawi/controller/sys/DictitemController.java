package cn.wawi.controller.sys;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.JsonRender;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Dictitem;

@ControllerBind(controllerKey="/sys_dictitem")
public class DictitemController extends BaseController<Dictitem> {
	
	public void list(){
		render(new JsonRender(findAll()).forIE());
	}

	@Override
	public String getSql() {
		return "select * from sys_dictitem where level!=2";
	}
}
