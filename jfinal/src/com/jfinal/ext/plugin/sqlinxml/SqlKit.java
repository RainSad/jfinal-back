/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.sqlinxml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.kit.JaxbKit;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;

public class SqlKit {

    protected static final Logger LOG = Logger.getLogger(SqlKit.class);

    private static Map<String, String> sqlMap;

    public static String sql(String groupNameAndsqlId) {
        if (sqlMap == null) {
            throw new NullPointerException("SqlInXmlPlugin not start");
        }
        return sqlMap.get(groupNameAndsqlId);
    }

    static void clearSqlMap() {
        sqlMap.clear();
    }

    static void init() {
        sqlMap = new HashMap<String, String>();
        List<File> files=new ArrayList<File>();
		refreshFileList(files,PathKit.getRootClassPath());
        for (File xmlfile : files) {
            SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
            String name = group.name;
            if (name == null || name.trim().equals("")) {
                name = xmlfile.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
            }
        }
        LOG.debug("sqlMap" + sqlMap);
    }
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
}
