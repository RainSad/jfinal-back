package cn.wawi.controller.sys;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wawi.utils.SystemUtils;

import com.baidu.ueditor.ActionEnter;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.render.JsonRender;

public class IndexController extends Controller{

	@ActionKey("/")
	public void login(){
		renderFreeMarker("/login.html");
	}
	@ActionKey("/index")
	public void index(){
		renderFreeMarker("/index.html");
	}
	@ActionKey("/ueditor/controller")
	public void ueditor(){
		String result=new ActionEnter( getRequest(), PathKit.getWebRootPath() ).exec();
		JSONObject json=null;
		System.out.println(result);
		try {
			json = new JSONObject(result);
			if(json.has("url")){
				json.put("url", SystemUtils.getRootPath()+json.getString("url"));
				result=json.toString();
			}else{
				String rootPath=PathKit.getWebRootPath().replaceAll("\\\\", "/");
				result= result.replaceAll(rootPath, SystemUtils.getRootPath());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		render(new JsonRender(result).forIE());
	}
}
