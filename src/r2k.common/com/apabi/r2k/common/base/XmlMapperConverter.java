package com.apabi.r2k.common.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XmlMapperConverter extends AbstractCollectionConverter {

	public XmlMapperConverter(Mapper mapper) {
		super(mapper);
	}

	@Override
	public boolean canConvert(Class type) {
		return type.equals(HashMap.class) || type.equals(Hashtable.class)
				|| type.getName().equals("java.util.LinkedHashMap")
				|| type.getName().equals("sun.font.AttributeMap");
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Map map = (Map) source;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			if(obj != null){
				Entry entry = (Entry)obj;
				if(entry.getValue()!=null){
					ExtendedHierarchicalStreamWriterHelper.startNode(writer,
							entry.getKey().toString(), Entry.class);
					writer.setValue(entry.getValue().toString());
					writer.endNode();
				}
			}
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Map map = (Map) createCollection(context.getRequiredType());  
        populateMap(reader, context, map);  
        return map; 
	}

	protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {  
        while (reader.hasMoreChildren()) {  
            reader.moveDown();  
            Object key = reader.getNodeName();  
            Object value = reader.getValue(); 
            map.put(key, value);  
            reader.moveUp();  
        }  
    } 
}
