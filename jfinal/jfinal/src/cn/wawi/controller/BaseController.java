package cn.wawi.controller;

import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.wawi.common.annotation.Permission;
import cn.wawi.utils.SqlHelper;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.render.JsonRender;

public abstract class BaseController<M extends Model<M>> extends Controller{

	private Class<M> clazz;
	private String tablename=null;
	private final Integer PAGE_SIZE=10;         //默认每页显示10条数据
	private final Integer PAGE_INDEX=1;         //默认当前页为第一页
	protected Map<String,Object> map=null;       //用于存储json数据
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<M>) pt.getActualTypeArguments()[0];
		this.tablename=TableMapping.me().getTable(clazz).getName();
		map=Maps.newHashMap();
		map.put("resCode", 1);
		map.put("resMsg", "请求成功!");
	}
	@Permission("view")
	public void main(){
		String pName=clazz.getPackage().getName();
		String main="/views/"+clazz.getPackage().getName().substring(pName.lastIndexOf(".")+1)+"/"+clazz.getSimpleName()+"/main.html";
		renderFreeMarker(main);
	}
	/**
	 * 通用分页查找
	 */
	public void findAllByPage(){
		Page<M> list=getModel(clazz).paginate(toPageIndex(), toPageSize(),getSql());
		map.put("rows",list.getList());
        map.put("total", list.getTotalRow());
        render(new JsonRender(map).forIE());
	}

	/**
	 * 通用查找全部
	 */
	public void findAll(){
		List<M> list=getModel(clazz).find(getSql());
        map.put("rows",list);
        map.put("total",list.size());
        render(new JsonRender(map).forIE());
	}


	/**
	 * 通用根据id查找
	 */
	public void findById(){
		M record=getModel(clazz).findById(tablename,getParaToInt("id"));
		render(new JsonRender(record).forIE());
	}

	/**
	 * 通用新增
	 */
	@Permission("add")
	public void addOne(){
		Model<?> m=getModel(clazz);
		if(!m.save()){
			map.put("resMsg", "修改失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}

	/**
	 * 通用修改
	 */
	@Permission("update")
	public void updateOne(){
		Model<?> m=getModel(clazz);
		if(!m.update()){
			map.put("resMsg", "修改失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}

	/**
	 * 通用删除
	 */
	@Permission("delete")
	public void delOne(){
		if(!getModel(clazz).delete()){
			map.put("resMsg", "删除失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}
	/**
	 * 批量id删除
	 */
	@Permission("delete")
	public void deleteBatch(){
		String ids=getPara("ids");
		if(Db.update("delete from "+tablename+" where id ("+ids+")")<=0){
			map.put("resMsg", "批量删除失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}
	/**
	 * 批量id更新
	 */
	@Permission("update")
	public void updateBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set status=0 where id in ("+ids+")")<=0){
			map.put("resMsg", "批量更新失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}

	/**
	 * 获取当前页
	 * @return 当前页码
	 */
	public int toPageIndex(){
		String pageIndex=getPara("page")==null?getPara("pageIndex"):getPara("page");
		if(StrKit.isBlank(pageIndex)){
			return PAGE_INDEX;
		}
		return Integer.parseInt(pageIndex);
	}
	/**
	 * 获取每页显示多少条数据
	 * @return 当前页码
	 */
	public int toPageSize(){
		String pageSize=getPara("rows")==null?getPara("pageSize"):getPara("rows");
		if(StrKit.isBlank(pageSize)){
			return PAGE_SIZE;
		}
		return Integer.parseInt(pageSize);
	}

	public  String getSql(){
		String sql=null;
		boolean flag=true;
		Map<String,Object> map=new HashMap<String,Object>();
		Enumeration<String> names=getParaNames();
		while(names.hasMoreElements()){
		   String name=(String) names.nextElement();
		   String value=getPara(name);
		   if(null!=value&&!"".equals(value.trim())){//过滤空值或空字符串
			   map.put(name, value.trim());
		   }
		   if("page".equals(name)||"rows".equals(name)){
		   }else{
			   flag=false;
		   }
		}
		if(flag){
			sql="select * from "+tablename;
		}else{
			sql=SqlHelper.getSql("cn.wawi.common.Dao.find"+clazz.getSimpleName(),map);
		}
		return sql;
	}
}
