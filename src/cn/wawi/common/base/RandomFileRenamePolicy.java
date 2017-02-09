package cn.wawi.common.base;

import java.io.File;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import cn.wawi.utils.StringUtil;

public class RandomFileRenamePolicy implements FileRenamePolicy{

	public File rename(File file) {
		File dest=new File(file.getParentFile().getAbsolutePath()+"\\"+StringUtil.getTimeFileName()+"$"+file.getName());
        File dir = dest.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
		return dest;
	}
}
