package cn.wawi.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;

public class DbUtil {

	public static void main(String[] args) {
		List<File> filelist=new ArrayList<File>();
		refreshFileList(filelist,PathKit.getRootClassPath());
		for (File xmlfile : filelist) {
			 System.out.println(xmlfile.getName());
		}
	}

	// 递归查找路径strPath下的所有sql.xml后缀的文件
	public static void refreshFileList(List<File> filelist,String strPath) {
		String filename;// 文件名
		File dir = new File(strPath);// 文件夹dir
		File[] files = dir.listFiles();// 文件夹下的所有文件或文件夹
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				refreshFileList(filelist,files[i].getAbsolutePath());// 递归文件夹！！！
			} else {
				filename = files[i].getName();
				if (filename.endsWith("sql.xml"))// 判断是不是msml后缀的文件
				{
					filelist.add(files[i]);// 对于文件才把它的路径加到filelist中
				}
			}

		}
	}

	/*
	 * 查询-easyui树
	 */
	public static List<Record> findTree(List<Record> list) {
		List<Record> permissions = new ArrayList<Record>();
		for (int i = 0; i < list.size(); i++) {
			Record p = list.get(i);
			if (p.get("parentId") == null || p.getInt("parentId") == 0) {
				List<Record> children = getChildren(list, p.get("id"));
				Record record = new Record();
				record.set("id", p.get("id"));
				record.set("text", p.get("name"));
				record.set("name", p.get("name"));
				record.set("url", p.get("url"));
				record.set("iconCls", p.get("iconCls"));
				if (children != null && children.size() > 0){
					record.set("children", children);
					record.set("state", "closed");
				}else{
					record.set("state", "open");
				}
				permissions.add(record);
			}
		}
		return permissions;
	}

	public static List<Record> getChildren(List<Record> list, Object parentId) {
		List<Record> children = new ArrayList<Record>();
		for (int i = 0; i < list.size(); i++) {
			Record p = list.get(i);
			if (parentId.equals(p.get("parentId"))) {
				Record record = new Record();
				record.set("id", p.get("id"));
				record.set("text", p.get("name"));
				record.set("name", p.get("name"));
				record.set("url", p.get("url"));
				record.set("iconCls", p.get("iconCls"));
				List<Record> c = getChildren(list, p.get("id"));
				if (c != null && c.size() > 0){
					record.set("children", c);
					record.set("state", "closed");
				}else{
					record.set("state", "open");
				}
				children.add(record);
			}
		}
		return children;
	}
}
