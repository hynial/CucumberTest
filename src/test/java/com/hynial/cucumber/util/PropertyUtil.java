package com.hynial.cucumber.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
    public static Properties getProperties(){
        Properties properties = new Properties();
//        InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("ContactConverter.properties");
        try {
//            properties.load(inputStream);
            // use InputStreamReader to fix chinese encode error.
            properties.load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream("wechat-bundle.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String getValue(String key){
        return getProperties().getProperty(key);
    }

}
