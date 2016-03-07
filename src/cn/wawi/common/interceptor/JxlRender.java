package cn.wawi.common.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import cn.wawi.utils.ExcelFilter;
import cn.wawi.utils.ExcelUtil;
import com.jfinal.log.Log;
import com.jfinal.render.Render;

public class JxlRender extends Render{
	
	protected static final Log LOG = Log.getLog(JxlRender.class);
	private static final String CONTENT_TYPE = "application/msexcel;charset="
			+ getEncoding();
	private List<?> data;
	private LinkedHashMap<String,String> fieldMap=new LinkedHashMap<String,String>();
	private String sheetname="sheet";
	public ExcelFilter filter=null;
	public JxlRender(List<?> data,LinkedHashMap<String,String> fieldMap,String sheetname){
		this.data=data;
		this.fieldMap=fieldMap;
		this.sheetname=sheetname;
	}
	@Override
	public void render() {
		String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
		this.response.reset();
		this.response.setHeader("Content-disposition", "attachment; filename="
				+fileName+".xls");
		this.response.setContentType(CONTENT_TYPE);
		OutputStream os = null;
		try {
			os = this.response.getOutputStream();
			ExcelUtil.listToExcel(data, fieldMap, sheetname,os,filter);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	public JxlRender setFilter(ExcelFilter filter) {
		this.filter = filter;
		return this;
	}

}
