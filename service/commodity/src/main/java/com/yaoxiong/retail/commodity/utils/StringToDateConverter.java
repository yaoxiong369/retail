package com.yaoxiong.retail.commodity.utils;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringToDateConverter implements Converter {
    @Override
    public Object convert(Class arg, Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new ConversionException(" 只支持字符串转换 ");
        }
        String str = (String) value;
        if (str.trim().equals("")) {
            return null;
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return sd.parse(str);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
