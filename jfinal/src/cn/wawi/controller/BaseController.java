package cn.wawi.controller;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.wawi.common.annotation.Permission;
import cn.wawi.utils.DateUtil;
import cn.wawi.utils.StringUtil;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ModelExt;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.render.JsonRender;

/**
 * <--------------基础控制器,拥有基本的增删改查功能-------------->
 * @author 龚亮
 * @date 2015-05-18 14:43:22
 */
public abstract class BaseController<M extends ModelExt<M>> extends Controller {
	
	private Class<M> clazz;
	private String tablename=null;
	private final Integer PAGE_SIZE=10;         //默认每页显示10条数据
	private final Integer PAGE_INDEX=1;         //默认当前页为第一页
	protected Map<String,Object> map=Maps.newHashMap();       //用于存储json数据
	private List<Object> params=new ArrayList<Object>();
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<M>) pt.getActualTypeArguments()[0];
		this.tablename=TableMapping.me().getTable(clazz).getName();
		map=new HashMap<String,Object>();
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
	public Map<String,Object> findAllByPage(){
		String sql=getSql();
		Object[] param=StringUtil.toArray(params);
		Page<M> list=getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql,param);
		map.put("rows",list.getList());
        map.put("total", list.getTotalRow());
        return map;
	}

	/**
	 * 通用查找全部
	 */
	public Map<String,Object> findAll(){
		String sql=getSql();
		Object[] param=StringUtil.toArray(params);
		List<M> list=param==null||param.length==0?getModel(clazz).find(sql):getModel(clazz).find(sql,param);
        map.put("rows",list);
        map.put("total",list.size());
        return map;
	}
	/**
	 * 通用分页查找
	 */
	public Map<String,Object> findAllByPage(String sql){
		Object[] param=StringUtil.toArray(params);
		Page<M> list=getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),sql,param);
		map.put("rows",list.getList());
        map.put("total", list.getTotalRow());
        return map;
	}

	/**
	 * 通用查找全部
	 */
	public Map<String,Object> findAll(String sql){
		Object[] param=StringUtil.toArray(params);
		List<M> list=param==null||param.length==0?getModel(clazz).find(sql):getModel(clazz).find(sql,param);
        map.put("rows",list);
        map.put("total",list.size());
        return map;
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
	public void save(){
		Model<?> m=getModel(clazz,"");
		if(StringUtil.notBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(m.getStr("password")));
			m.set("inputTime", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
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
	public void update(){
		Model<?> m=getModel(clazz,"");
		if(StringUtil.notBlank(m.getStr("password"))){
			m.set("password", HashKit.md5(m.getStr("password")));
		}
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
	public void delete(){
		if(!getModel(clazz,"").delete()){
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
		String[] ids=getPara("ids").split(",");
		Object[] o=new Object[ids.length];
		String sql="(";
		for (int i = 0; i < ids.length-1; i++) {
			sql+="?,";
			o[i]=ids[i];
		}
		sql+="?)";
		o[ids.length-1]=ids[ids.length-1];
		if(Db.update("delete from "+tablename+" where id in"+sql,o)<=0){
			map.put("resMsg", "删除失败!");
			map.put("resCode", 0);
		}
		render(new JsonRender(map).forIE());
	}
	/**
	 * 批量id更新
	 */
	@Permission("update")
	public void updateBatch(){
		String[] ids=getPara("ids").split(",");
		Object[] o=new Object[ids.length];
		String sql="(";
		for (int i = 0; i < ids.length-1; i++) {
			sql+="?,";
			o[i]=ids[i];
		}
		sql+="?)";
		o[ids.length-1]=ids[ids.length-1];
		if(Db.update("update "+tablename+" set status=0 where id in"+sql,o)<=0){
			map.put("resMsg", "删除失败!");
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
		if(pageIndex==null||pageIndex.trim().equals("")){
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
		if(pageSize==null||pageSize.trim().equals("")){
			return PAGE_SIZE;
		}
		return Integer.parseInt(pageSize);
	}
	
	/*
	 * 构建查询sql
	 */
	public void buildSql(StringBuilder sb){
		if(StringUtil.notBlank(getPara("startTime"))){
			if(sb.toString().toLowerCase().contains("where")){
				sb.append(" AND inputTime>=? ");
			}else{
				sb.append(" WHERE inputTime>=? ");
			}
			params.add(getPara("startTime"));
		}
		if(StringUtil.notBlank(getPara("endTime"))){
			if(sb.toString().toLowerCase().contains("where")){
				sb.append(" AND inputTime<=? ");
			}else{
				sb.append(" WHERE inputTime<=? ");
			}
			params.add(getPara("endTime"));
		}
		if(StringUtil.notBlank(getPara("status"))){
			if(sb.toString().toLowerCase().contains("where")){
				sb.append(" AND status=? ");
			}else{
				sb.append(" WHERE status=? ");
			}
			params.add(getPara("status"));
		}
	}
	public abstract String getSql();
	/*
	 * 设置查询参数
	 */
	public void setParams(Object value){
		params.add(value);
	}
}
