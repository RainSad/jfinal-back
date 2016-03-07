package cn.wawi.common.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 返回前台的json数据
 * @author 龚亮
 * @date 2015-05-18 14:42:28
 */

public class ResultJson {

	private String resMsg="请求成功!";         //是否请求成功
	private Integer resCode=1;     //1 请求成功 0请求失败
	private String method="";         //方法名
	private String methodDes="";      //接口描述
	private Map<String,Object> resData=new HashMap<String,Object>();          //数据集合
	public ResultJson(){}
	public ResultJson(Map<String,Object> resData,String method,String methodDes){
		this.method=method;
		this.methodDes=methodDes;
		this.resData=resData;
	} 
	public ResultJson(String method,String methodDes){
		this.method=method;
		this.methodDes=methodDes;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMethodDes() {
		return methodDes;
	}
	public void setMethodDes(String methodDes) {
		this.methodDes = methodDes;
	}
	public Integer getResCode() {
		return resCode;
	}
	public void setResCode(Integer resCode) {
		this.resCode = resCode;
	}
	public Map<String, Object> getResData() {
		return resData;
	}
	public void setResData(Map<String, Object> resData) {
		this.resData = resData;
	}
}
