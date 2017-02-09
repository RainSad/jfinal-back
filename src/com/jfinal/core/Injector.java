/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.jfinal.core;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

public class Injector {
	private static <T> T createInstance(Class<T> objClass) {
		try {
			return objClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T injectModel(Class<T> modelClass, HttpServletRequest request, boolean skipConvertError) {
		String modelName = modelClass.getSimpleName();
		return injectModel(modelClass, StrKit.firstCharToLowerCase(modelName), request, skipConvertError);
	}

	public static <T> T injectBean(Class<T> beanClass, HttpServletRequest request, boolean skipConvertError) {
		String beanName = beanClass.getSimpleName();
		return injectBean(beanClass, StrKit.firstCharToLowerCase(beanName), request, skipConvertError);
	}

	@SuppressWarnings("unchecked")
	public static final <T> T injectBean(Class<T> beanClass, String beanName, HttpServletRequest request,
			boolean skipConvertError) {
		Object bean = createInstance(beanClass);
		String modelNameAndDot = (StrKit.notBlank(beanName)) ? beanName + "." : null;

		Map<String, String[]> parasMap = request.getParameterMap();
		Method[] methods = beanClass.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (!(methodName.startsWith("set")))
				continue;
			if (methodName.length() <= 3) {
				continue;
			}
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 1) {
				continue;
			}

			String attrName = StrKit.firstCharToLowerCase(methodName.substring(3));
			String paraName = (modelNameAndDot != null) ? modelNameAndDot + attrName : attrName;
			if (!(parasMap.containsKey(paraName)))
				continue;
			try {
				String paraValue = request.getParameter(paraName);
				Object value = (paraValue != null) ? TypeConverter.convert(types[0], paraValue) : null;
				method.invoke(bean, new Object[] { value });
			} catch (Exception e) {
				if (!(skipConvertError)) {
					throw new RuntimeException(e);
				}
			}

		}

		return (T)bean;
	}

	@SuppressWarnings("unchecked")
	public static final <T> T injectModel(Class<T> modelClass, String modelName, HttpServletRequest request,
			boolean skipConvertError) {
		Object temp = createInstance(modelClass);
		if (!(temp instanceof Model)) {
			throw new IllegalArgumentException("getModel only support class of Model, using getBean for other class.");
		}

		Model<?> model = (Model<?>)temp;
		Table table = TableMapping.me().getTable(model.getClass());
		if (table == null) {
			throw new ActiveRecordException("The Table mapping of model: " + modelClass.getName()
					+ " not exists or the ActiveRecordPlugin not start.");
		}

		String modelNameAndDot = (StrKit.notBlank(modelName)) ? modelName + "." : null;
		Map<String, String[]> parasMap = request.getParameterMap();
		Map<String, Class<?>> columnTypeMap = table.getColumnTypeMap();
		for(Entry<String, Class<?>> entry: columnTypeMap.entrySet()){
			for(Entry<String,String[]> e: parasMap.entrySet()){
				String attrName;
				Class<?> colType = entry.getValue();
				attrName= entry.getKey();
				if (colType == null) {
					if (skipConvertError) {
						continue;
					}
					throw new ActiveRecordException("The model attribute " + attrName + " is not exists.");
				}
            	String paraName = (String) e.getKey();
				if (modelNameAndDot != null) {
					if(paraName.startsWith(modelNameAndDot)){
						paraName= paraName.substring(modelNameAndDot.length());
					}
				} 
            	if(paraName.replaceAll("_", "").equalsIgnoreCase(entry.getKey().replaceAll("_", ""))){
    				try {
	                    String[] paraValue = parasMap.get(e.getKey());
	                    Object value = paraValue[0] != null ? TypeConverter.convert(colType, paraValue[0]) : null;
    					model.set(attrName, value);
    				} catch (Exception exception) {
    					if (!(skipConvertError)) {
    						throw new RuntimeException("Can not convert parameter: " + e.getKey(), exception);
    					}
    				}
            	}
			}
		}
		return (T)model;
	}
}