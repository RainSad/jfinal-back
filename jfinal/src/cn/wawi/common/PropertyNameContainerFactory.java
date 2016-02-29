package cn.wawi.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import cn.wawi.utils.StringUtil;

import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.IContainerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyNameContainerFactory implements IContainerFactory {
	
	public Object convert(Object key) {
        if (key instanceof String) {
           String s=key.toString();
           if(s.contains("_")){
               String str[]=s.split("_");
           	   String s1=str[0].toLowerCase();
               for (int i = 1; i < str.length; i++) {
    			s1+=StringUtil.firstCharToUpperCase(str[i].toLowerCase());
    		   }
               s=s1;
           }
           if("parentId".equals(s)){
        	   s="_parentId";
           }
           return s;
        }
        return key;
    }
 
    public PropertyNameContainerFactory() {
    }
 
 
    public Map<String, Object> getAttrsMap() {
        return new PropertyNameMap();
    }
 
    public Map<String, Object> getColumnsMap() {
        return Maps.newHashMap();
    }
 
    public Set<String> getModifyFlagSet() {
        return new PropertyNameSet();
    }
 
    public class PropertyNameSet extends HashSet {
        
		private static final long serialVersionUID = 1L;

		public boolean add(Object e) {
            return super.add(convert(e));
        }
 
        public boolean remove(Object e) {
            return super.remove(convert(e));
        }
 
        public boolean contains(Object e) {
            return super.contains(convert(e));
        }
 
        public boolean addAll(Collection c) {
            boolean modified = false;
            for (Object o : c)
                if (super.add(convert(o)))
                    modified = true;
            return modified;
        }
    }
 
    public class PropertyNameMap extends HashMap {

		private static final long serialVersionUID = 1L;

		public Object get(Object key) {
            return super.get(convert(key));
        }
 
        public boolean containsKey(Object key) {
            return super.containsKey(convert(key));
        }
 
        public Object put(Object key, Object value) {
        	if(key.equals("parentId")||key.equals("parent_id")){
        		super.put("state", "closed");
        	}
            return super.put(convert(key), value);
        }
 
        public Object remove(Object key) {
            return super.remove(convert(key));
        }
    }
}