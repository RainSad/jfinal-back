package cn.wawi.controller.sys;

import cn.wawi.common.base.R;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Resources;
import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_resources")
public class ResourcesController extends BaseController<Resources>{
	
	@Override
	public String getSql() {
    	String sql="select * from "+tablename;
		return sql;
	}

	public void saveBasePermission(){
		int parentId=getParaToInt("parentId");
		int level=getParaToInt("level");
		List<Resources> lists=Lists.newArrayList();
		Resources save=new Resources("添加","create","icon-add",parentId,level,1);
		Resources update=new Resources("修改","update","icon-edit",parentId,level,2);
		Resources delete=new Resources("删除","delete","icon-remove",parentId,level,3);
		Resources list=new Resources("查询","list","icon-search",parentId,level,4);
		lists.add(save);
		lists.add(update);
		lists.add(delete);
		lists.add(list);
		Db.batchSave(lists, 10);
		render(new JsonRender(success("添加基本权限成功!")).forIE());
	}
	public void getRoleResources(){
		R<Record> r=new R<Record>(1,"查询用户角色成功!");
		int roleId=getParaToInt("roleId");
		List<Record> roles=Resources.dao.getRoleResources(roleId);
		r.put(roles);
        render(new JsonRender(r).forIE());
	}
	@Before(Tx.class)
	public void saveRoleResources(){
		R<Record> r=new R<Record>(1,"保存用户角色成功!");
		int roleId=getParaToInt("roleId");
		Integer[] resourcesIds=getParaValuesToInt("resourcesIds[]");
		Db.update("delete from sys_role_resources where role_id=?",roleId);
		if(resourcesIds!=null&&resourcesIds.length>0){
			for(int resourcesId: resourcesIds){
				Record record=new Record();
				record.set("resources_id", resourcesId);
				record.set("role_id", roleId);
				Db.save("sys_role_resources", record);
			}
		}
        render(new JsonRender(r).forIE());
	}
}
