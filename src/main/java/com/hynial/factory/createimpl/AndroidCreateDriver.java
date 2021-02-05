package com.hynial.factory.createimpl;

import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.factory.icreate.ICreateDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidCreateDriver implements ICreateDriver {
    private ConfigLoader configLoader;

    public AndroidCreateDriver(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public AppiumDriver create() {
        DesiredCapabilities desiredCapabilities = configLoader.loadAndroidCapabilities().getDesiredCapabilities();
        URL remoteUrl = null;

        try {
            System.out.println("CreateDriver...\n" + configLoader.loadAndroidCapabilities().description());
            remoteUrl = new URL(configLoader.loadAppiumInfo().getAppiumUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        AppiumDriver appiumDriver = new AndroidDriver(remoteUrl, desiredCapabilities);
        initAppiumDriverSettings(appiumDriver);

        String udid = appiumDriver.getCapabilities().getCapability("udid").toString();
//        udid = desiredCapabilities.getCapability("udid").toString();
        System.out.println("Udid:" + udid + ", SessionId:" + appiumDriver.getSessionId() + ", RemoteUrl:" + appiumDriver.getRemoteAddress().toString());
        return appiumDriver;
    }

    private void initAppiumDriverSettings(AppiumDriver appiumDriver){
        appiumDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // timeout setting https://devqa.io/webdriver-explicit-implicit-fluent-wait/
    }
}
