package com.apabi.r2k.common.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XmlDateConverter extends AbstractCollectionConverter {

	public XmlDateConverter(Mapper mapper) {
		super(mapper);
	}

	private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public boolean canConvert(Class type) {
		return Date.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Date date = (Date) source;  
        writer.setValue(format(DATE_FORMAT, date));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		try {  
            return parse(DATE_FORMAT, reader.getValue());  
        } catch (Exception e) {  
            try {  
                return parse("yyyy-MM-dd", reader.getValue());  
            } catch (Exception e1) {  
                e1.printStackTrace();  
            }  
            throw new ConversionException(e.getMessage(), e);  
        }  
	}

	public static String format(String pattern, Date date) {  
        if (date == null) {  
            return "";  
        } else {  
            return new SimpleDateFormat(pattern).format(date);  
        }  
    }  
  
    public static Date parse(String pattern, String text) throws Exception{  
        if(text==null||"".equals(text.trim())||"null".equals(text.trim().toLowerCase()))  
        {  
            return null;  
        }  
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);  
        return dateFormat.parse(text);  
    }  
}
