package com.hynial.cucumber.config;

import com.hynial.cucumber.util.PropertyUtil;
import com.hynial.wechat.entity.mobiledriver.AndroidCapabilities;
import com.hynial.wechat.entity.mobiledriver.AppiumInfo;
import io.cucumber.guice.ScenarioScoped;

import java.util.Properties;

@ScenarioScoped
public class ConfigLoader {
    private static Properties properties;
    private PropertyUtil propertyUtil = new PropertyUtil(properties);

    private static String getConfigContext(){
        String context = System.getProperty("context");
        if(context == null){
            context = "context-wechat";
        }

        return context;
    }

    static {
        if(properties == null) {
            properties = PropertyUtil.getProperties(getConfigContext());
        }
    }

    private static ConfigLoader instance;

/*    private ConfigLoader() {
    }*/

    public static ConfigLoader getInstance(){
        if(instance == null) {
            return new ConfigLoader();
        }

        return instance;
    }

    private AppiumInfo appiumInfo = new AppiumInfo(propertyUtil.getString(AppiumInfo.APPIUM_HOST), propertyUtil.getString(AppiumInfo.APPIUM_PORT));
    private AndroidCapabilities androidCapabilities = new AndroidCapabilities(propertyUtil);

    public AppiumInfo loadAppiumInfo(){
        return appiumInfo;
    }

    public AndroidCapabilities loadAndroidCapabilities(){
        return androidCapabilities;
    }
}
