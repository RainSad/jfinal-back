package com.jfinal.plugin.activerecord.generator;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.JavaKeyword;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BaseModelGenerator {
	protected String packageTemplate = "package %s;%n%n";

	protected String importTemplate = "import com.jfinal.plugin.activerecord.Model;%nimport com.jfinal.plugin.activerecord.IBean;%n%n";

	protected String classDefineTemplate = "/**%n * Generated by JFinal, do not modify this file.%n */%n@SuppressWarnings(\"serial\")%npublic abstract class %s<M extends %s<M>> extends Model<M> implements IBean {%n%n";

	protected String setterTemplate = "\tpublic void %s(%s %s) {%n\t\tset(\"%s\", %s);%n\t}%n%n";

	protected String getterTemplate = "\tpublic %s %s() {%n\t\treturn get(\"%s\");%n\t}%n%n";
	protected String baseModelPackageName;
	protected String baseModelOutputDir;
	protected JavaKeyword javaKeyword = new JavaKeyword();

	public BaseModelGenerator(String baseModelPackageName,
			String baseModelOutputDir) {
		if (StrKit.isBlank(baseModelPackageName))
			throw new IllegalArgumentException(
					"baseModelPackageName can not be blank.");
		if ((baseModelPackageName.contains("/"))
				|| (baseModelPackageName.contains("\\")))
			throw new IllegalArgumentException("baseModelPackageName error : "
					+ baseModelPackageName);
		if (StrKit.isBlank(baseModelOutputDir)) {
			throw new IllegalArgumentException(
					"baseModelOutputDir can not be blank.");
		}
		this.baseModelPackageName = baseModelPackageName;
		this.baseModelOutputDir = baseModelOutputDir;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate base model ...");
		for (TableMeta tableMeta : tableMetas)
			genBaseModelContent(tableMeta);
		wirtToFile(tableMetas);
	}

	protected void genBaseModelContent(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		genPackage(ret);
		genImport(tableMeta,ret);
		genClassDefine(tableMeta, ret);
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genSetMethodName(columnMeta, ret);
			genGetMethodName(columnMeta,tableMeta, ret);
		}
		ret.append(String.format("}%n", new Object[0]));
		tableMeta.baseModelContent = ret.toString();
	}

	protected void genPackage(StringBuilder ret) {
		ret.append(String.format(this.packageTemplate,
				new Object[] { this.baseModelPackageName }));
	}

	protected void genImport(StringBuilder ret) {
		ret.append(String.format(this.importTemplate, new Object[0]));
	}
	protected void genImport(TableMeta tableMeta,StringBuilder ret) {
//		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
//			if(columnMeta.attrName.equalsIgnoreCase(tableMeta.primaryKey.replaceAll("_", ""))&&!columnMeta.attrName.equals("id")){
//				ret.append("import com.fasterxml.jackson.annotation.JsonProperty;\r\n");
//			}
//			if(columnMeta.javaType.equals("java.util.Date")){
//				ret.append("import com.fasterxml.jackson.annotation.JsonFormat;\r\n");
//			}
//		}
		ret.append(String.format(this.importTemplate, new Object[0]));
	}
	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(this.classDefineTemplate, new Object[] {
				tableMeta.baseModelName, tableMeta.baseModelName }));
	}

	protected void genSetMethodName(ColumnMeta columnMeta, StringBuilder ret) {
		String setterMethodName = "set"
				+ StrKit.firstCharToUpperCase(columnMeta.attrName);

		String argName = (this.javaKeyword.contains(columnMeta.attrName)) ? "_"
				+ columnMeta.attrName : columnMeta.attrName;
		String setter = String.format(this.setterTemplate, new Object[] {
				setterMethodName, columnMeta.javaType, argName,
				columnMeta.name, argName });
		ret.append(setter);
	}

	protected void genGetMethodName(ColumnMeta columnMeta,TableMeta tableMeta, StringBuilder ret) {
//		if(columnMeta.attrName.equalsIgnoreCase(tableMeta.primaryKey.replaceAll("_", ""))&&!columnMeta.attrName.equals("id")){
//			ret.append("\t@JsonProperty(\"id\")\r\n");
//		}
//		if(columnMeta.javaType.equals("java.util.Date")){
//			ret.append("\t	@JsonFormat(pattern=\"yyyy-MM-dd HH:mm:ss\")\r\n");
//		}
		//System.out.println(columnMeta.javaType+":"+tableMeta.primaryKey);
		String getterMethodName = "get"
				+ StrKit.firstCharToUpperCase(columnMeta.attrName);
		String getter = String.format(this.getterTemplate, new Object[] {
				columnMeta.javaType, getterMethodName, columnMeta.name });
		ret.append(getter);
	}
	protected void genGetMethodName(ColumnMeta columnMeta, StringBuilder ret) {
		String getterMethodName = "get"
				+ StrKit.firstCharToUpperCase(columnMeta.attrName);
		
		String getter = String.format(this.getterTemplate, new Object[] {
				columnMeta.javaType, getterMethodName, columnMeta.name });
		ret.append(getter);
	}

	protected void wirtToFile(List<TableMeta> tableMetas) {
		try {
			for (TableMeta tableMeta : tableMetas)
				wirtToFile(tableMeta);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void wirtToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(this.baseModelOutputDir);
		if (!(dir.exists())) {
			dir.mkdirs();
		}
		String target = this.baseModelOutputDir + File.separator
				+ tableMeta.baseModelName + ".java";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.baseModelContent);
		} finally {
			fw.close();
		}
	}
}