package cn.wawi.controller;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.wawi.common.base.R;
import cn.wawi.model.sys.User;

import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.render.JsonRender;

public abstract class BaseController<M extends Model<M>> extends Controller{

	private Class<M> clazz;
	protected String tablename=null;
	private final Integer PAGE_SIZE=10;         //默认每页显示10条数据
	private final Integer PAGE_INDEX=1;         //默认当前页为第一页
	
	protected List<Object> params=new ArrayList<Object>();
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<M>) pt.getActualTypeArguments()[0];
		this.tablename=TableMapping.me().getTable(clazz).getName();
	}
	public void main(){
		String pName=clazz.getPackage().getName();
		String main="/views/"+clazz.getPackage().getName().substring(pName.lastIndexOf(".")+1)+"/"+clazz.getSimpleName()+"/main.html";
		renderFreeMarker(main);
	}
	/**
	 * 通用分页查找
	 */
	public void findAllByPage(){
		String sql=getSql();
		Page<M> list=params==null||params.size()==0?getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql):getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql,params.toArray());
        render(new JsonRender(success("请求成功!").put(list.getList(), list.getTotalRow()+0L)).forIE());
	}

	/**
	 * 通用查找全部
	 */
	public void findAll(){
		String sql=getSql();
		List<M> list=params==null||params.size()==0?getModel(clazz).find(sql):getModel(clazz).find(sql,params.toArray());
        render(new JsonRender(success("请求成功").put(list)).forIE());
	}


	/**
	 * 通用根据id查找
	 */
	public void findById(){
		M record=getModel(clazz).findById(getParaToInt("id"));
		render(new JsonRender(success("查找id成功!").put(record)).forIE());
	}

	/**
	 * 通用新增
	 */
	public void saveORupdate(){
		M m=getModel(clazz);
		boolean flag=false;
		if(m instanceof User&&StrKit.notBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(HashKit.sha1(m.getStr("username")+m.getStr("password"))));
		}
		if(m instanceof User&&StrKit.isBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(HashKit.sha1(m.getStr("username")+"123456")));
		}
		if(null!=m.get(TableMapping.me().getTable(clazz).getPrimaryKey()[0])){
			flag=m.update();
		}else{
			flag=m.save();
		}
		if(flag){
			render(new JsonRender(success("保存或更新数据成功!").put(m)).forIE());
		}else{
			render(new JsonRender(success("保存或更新数据失败!").put(m)).forIE());
		}
	}

	/**
	 * 通用删除
	 */
	public void delOne(){
		if(!getModel(clazz).delete()){
			render(new JsonRender(error("删除失败!")).forIE());
		}else{
			render(new JsonRender(success("删除成功!")).forIE());
		}
	}
	/**
	 * 批量id删除
	 */
	public void deleteBatch(){
		Integer[] ids=getParaValuesToInt("ids[]");
		for(int id: ids)
			getModel(clazz).deleteById(id);
		render(new JsonRender(success("批量删除成功!")).forIE());
	}
	/**
	 * 批量id更新状态
	 */
	public void updateBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set status=0 where "+TableMapping.me().getTable(clazz).getPrimaryKey()[0]+" in (?)",ids)<=0){
			render(new JsonRender(error("批量更新失败!")).forIE());
		}else{
			render(new JsonRender(success("批量更新成功!")).forIE());
		}
	}

	/**
	 * 获取当前页
	 * @return 当前页码
	 */
	protected int toPageIndex(){
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
	protected int toPageSize(){
		String pageSize=getPara("rows")==null?getPara("pageSize"):getPara("rows");
		if(StrKit.isBlank(pageSize)){
			return PAGE_SIZE;
		}
		return Integer.parseInt(pageSize);
	}

	/**
	 * 获取动态sql
	 */
	protected abstract String getSql();
	
	public String getPara(String name) {
		if(StrKit.notBlank(getRequest().getParameter(name))){
			return getRequest().getParameter(name);
		}
		return null;
	}
	protected R<M> success(String resMsg){
		return new R<M>(resMsg);
	}
	protected R<M> error(String resMsg){
		return new R<M>(0,resMsg);
	}
	protected User getUser(){
		return getSessionAttr("loginUser");
	}
	public Map<String,String> getparamaterMap(){
		Map<String,String> params=Maps.newHashMap();
		Map<String,String[]> param=getParaMap();
		for(String key: param.keySet()){
			params.put(key, getPara(key));
		}
		return params;
	}
}
