package com.apabi.r2k.common.base;

import java.util.List;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XmlListConverter extends CollectionConverter {

	public XmlListConverter(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	public boolean canConvert(Class type) {
		return super.canConvert(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		List list = (List) source;
		writer.addAttribute("count", String.valueOf(list.size()));
		super.marshal(source, writer, context);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return super.unmarshal(reader, context);
	}

}
