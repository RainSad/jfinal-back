package cn.wawi.model.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseResources<M extends BaseResources<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setUrl(java.lang.String url) {
		set("url", url);
	}

	public java.lang.String getUrl() {
		return get("url");
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parent_id", parentId);
	}

	public java.lang.Integer getParentId() {
		return get("parent_id");
	}

	public void setPermCode(java.lang.String permCode) {
		set("perm_code", permCode);
	}

	public java.lang.String getPermCode() {
		return get("perm_code");
	}

	public void setIconCls(java.lang.String iconCls) {
		set("iconCls", iconCls);
	}

	public java.lang.String getIconCls() {
		return get("iconCls");
	}

	public void setLevel(java.lang.Integer level) {
		set("level", level);
	}

	public java.lang.Integer getLevel() {
		return get("level");
	}

	public void setSort(java.lang.Integer sort) {
		set("sort", sort);
	}

	public java.lang.Integer getSort() {
		return get("sort");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
