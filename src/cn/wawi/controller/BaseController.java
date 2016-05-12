package cn.wawi.controller;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.ResultData;
import cn.wawi.model.sys.User;

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
	protected ResultData<M> json=null;       //用于存储json数据
	protected List<Object> params=new ArrayList<Object>();
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<M>) pt.getActualTypeArguments()[0];
		this.tablename=TableMapping.me().getTable(clazz).getName();
		json=new ResultData<M>();
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
		String sql=getSql();
		Page<M> list=params==null||params.size()==0?getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql):getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql,params.toArray());
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}

	/**
	 * 通用查找全部
	 */
	public void findAll(){
		String sql=getSql();
		List<M> list=params==null||params.size()==0?getModel(clazz).find(sql):getModel(clazz).find(sql,params.toArray());
		json.setRows(list);
		json.setTotal(list.size()+0L);
        render(new JsonRender(json).forIE());
	}


	/**
	 * 通用根据id查找
	 */
	public void findById(){
		M record=getModel(clazz).findById(getParaToInt("id"));
		List<M> list=new ArrayList<M>();
		list.add(record);
		json.setRows(list);
		json.setTotal(1L);
		render(new JsonRender(json).forIE());
	}

	/**
	 * 通用新增
	 */
	@Permission("add")
	public void addOne(){
		M m=getModel(clazz);
		if(m instanceof User&&StrKit.notBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(m.getStr("password")));
		}
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}else{
			List<M> list=new ArrayList<M>();
			list.add(m);
			json.setRows(list);
			json.setTotal(1L);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * 通用修改
	 */
	@Permission("update")
	public void updateOne(){
		M m=getModel(clazz);
		if(m instanceof User&&StrKit.notBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(m.getStr("password")));
		}
		if(!m.update()){
			json.setResMsg("修改失败!");
			json.setResCode(0);
		}else{
			List<M> list=new ArrayList<M>();
			list.add(m);
			json.setRows(list);
			json.setTotal(1L);
		}
		render(new JsonRender(json).forIE());
	}
	/**
	 * 通用删除
	 */
	@Permission("delete")
	public void delOne(){
		if(!getModel(clazz).delete()){
			json.setResMsg("删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	/**
	 * 批量id删除
	 */
	@Permission("delete")
	public void deleteBatch(){
		String ids=getPara("ids");
		if(Db.update("delete from "+tablename+" where "+TableMapping.me().getTable(clazz).getPrimaryKey()[0]+" in (?)",ids)<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * 批量id更新状态
	 */
	@Permission("update")
	public void updateBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set status=0 where "+TableMapping.me().getTable(clazz).getPrimaryKey()[0]+" in (?)",ids)<=0){
			json.setResMsg("批量更新失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
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

	/**
	 * 获取动态sql
	 */
	public abstract String getSql();
	
	public String getPara(String name) {
		if(StrKit.notBlank(getRequest().getParameter(name))){
			return getRequest().getParameter(name);
		}
		return null;
	}
}
