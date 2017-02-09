package cn.wawi.controller.sys;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import com.baidu.ueditor.ActionEnter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import cn.wawi.common.base.KaptchaRender;
import cn.wawi.utils.StringUtil;

@Clear
public class IndexController extends Controller{

	@ActionKey("/")
	public void login(){
		renderFreeMarker("/login.html");
	}
	@ActionKey("/kaptcha")
	public void getKaptcha(){
		render(new KaptchaRender());
	}
	
	@ActionKey("/ueditor/config")
	public void ueditor(){
		String result=new ActionEnter(getRequest(),PathKit.getWebRootPath()).exec();
		render(new JsonRender(result).forIE());
	}
	@ActionKey("/ueditor/uploadImage")
	public void uploadImage(){
        render(new JsonRender(getUrl("ueditor/image/")).forIE());
	}
	@ActionKey("/ueditor/uploadFile")
	public void uploadFile(){
        render(new JsonRender(getUrl("ueditor/file/")).forIE());
	}
	@ActionKey("/ueditor/uploadVideo")
	public void uploadVideo(){
		 render(new JsonRender(getUrl("ueditor/video/")).forIE());
	}
	@ActionKey("/ueditor/listFile")
	public void listFile(){
        render(new JsonRender(getUrls(PropKit.get("fileUploadPath")+"ueditor/file/")).forIE());
	}
	@ActionKey("/ueditor/listImage")
	public void listImage(){
        render(new JsonRender(getUrls(PropKit.get("fileUploadPath")+"ueditor/image/")).forIE());
	}
	private Map<String,Object> getUrls(String filename){
        Map<String,Object> m=Maps.newHashMap();
        m.put("state", "SUCCESS");
        m.put("total", getParaToInt("size"));
        m.put("start", getParaToInt("start"));
        File file=new File(filename);
    	if(!file.exists()&&!file.isDirectory())
    		file.mkdirs();
		Collection<File> list=FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter(""), DirectoryFileFilter.INSTANCE);
		List<Map<String,Object>> urls=Lists.newArrayList();
		for(File f: list){
			String path=f.getAbsolutePath();
			path=path.replaceAll("\\\\", "/");
			Map<String,Object> url=Maps.newHashMap();
			url.put("state", "SUCCESS");
			url.put("url", path.replaceAll(PropKit.get("fileUploadPath"), PropKit.get("fileDownPath")));
			urls.add(url);
		}
		m.put("list", urls);
		return m;
	}
	private Map<String,Object> getUrl(String filenamePrex){
        Map<String,Object> m=Maps.newHashMap();
        String filePath=StringUtil.generateRandomDateDir(filenamePrex);
        UploadFile file=getFile("upfile", filePath);
        m.put("state", "SUCCESS");
        m.put("url", PropKit.get("fileDownPath")+filePath+file.getFileName());
        m.put("title", file.getFileName());
        m.put("original", getPara("name"));
        m.put("type", getPara("type"));
        m.put("size", getParaToInt("size"));
		return m;
	}
}
