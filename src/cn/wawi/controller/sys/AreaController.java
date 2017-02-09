package cn.wawi.controller.sys;

import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Area;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/sys_area")
public class AreaController extends BaseController<Area>{

	
	@Override
	public String getSql() {
		Integer parentId=getParaToInt("parentId");
    	String sql="select * from "+tablename;
    	if(parentId==null){
    		sql+=" where parent_id is null";
    	}else if(parentId!=null&&-1==parentId){
    		sql+=" where parent_id=0 OR level=2";
    	}else if(parentId!=null&&-1!=parentId){
    		sql+=" where parent_id="+parentId;
    	}
		return sql;
	}
}
