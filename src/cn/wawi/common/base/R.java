package cn.wawi.common.base;

import java.util.List;
import com.google.common.collect.Lists;
/**
 * @description 返回前台的json数据
 * @author 龚亮
 * @date 2015-05-18 14:42:28
 */
public class R<M>{

	private String resMsg;         
	/*
     * 1   请求成功 
	 * 0      请求失败
	 */
	private int resCode;   
    private Long total;
    private List<M> rows=Lists.newArrayList();
    /**
     * 额外的参数
     */
    private M extra;
    

    public R(String resMsg){
    	this();
    	this.resMsg=resMsg;
    }
    public R(int resCode,String resMsg){
    	this();
    	this.resCode=resCode;
    	this.resMsg=resMsg;
    }
	public R(){
		resCode=1;
		total=0L;
		resMsg="请求成功!";
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<M> rows) {
		this.rows = rows;
	}
	public int getResCode() {
		return resCode;
	}
	public void setResCode(int resCode) {
		this.resCode = resCode;
	}
	public M getExtra() {
		return extra;
	}
	public void setExtra(M extra) {
		this.extra = extra;
	}
	public R<M> put(List<M> rows,Long total){
		this.rows=rows;
		this.total=total;
		return this;
	}
	public R<M> put(List<M> rows){
		this.rows=rows;
		this.total=rows.size()+0L;
		return this;
	}
	public R<M> put(List<M> rows,int total){
		this.rows=rows;
		this.total=total+0L;
		return this;
	}
	public R<M> put(M row){
		this.rows.add(row);
		this.total=1L;
		return this;
	}
	public R<M> putExtra(M extra){
		this.extra=extra;
		return this;
	}
}
