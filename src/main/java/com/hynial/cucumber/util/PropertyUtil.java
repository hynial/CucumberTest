package com.hynial.cucumber.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class PropertyUtil {

    public static Properties getProperties(String bundleFolder){
        Properties properties = new Properties();
        try {
            FileResourceUtil fileResourceUtil = new FileResourceUtil();
            List<File> propertyFiles = fileResourceUtil.getFilesFromResourceBySuffix(bundleFolder, ".properties");
            propertyFiles.forEach(file -> {
                String bundleName = bundleFolder + File.separator + file.getName();
                try {
                    properties.load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(bundleName), StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return properties;
    }

    private Properties instanceProperties;

    public PropertyUtil(Properties instanceProperties) {
        this.instanceProperties = instanceProperties;
    }

    public String getString(String key){
        return this.instanceProperties.getProperty(key);
    }

    public boolean getBoolean(String key){
        return Boolean.parseBoolean(this.instanceProperties.getProperty(key));
    }

    public int getInt(String key){
        return Integer.parseInt(this.instanceProperties.getProperty(key));
    }

    public long getLong(String key){
        return Long.parseLong(this.instanceProperties.getProperty(key));
    }

    public double getDouble(String key){
        return Double.parseDouble(this.instanceProperties.getProperty(key));
    }

    public static void main(String[] args) {
        System.out.println(PropertyUtil.getProperties("context-wechat").getProperty("appPackage"));
        System.out.println(PropertyUtil.getProperties(".").getProperty("cucumber.publish.quiet"));
    }
}
