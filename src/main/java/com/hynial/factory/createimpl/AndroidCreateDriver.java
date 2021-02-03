package com.hynial.factory.createimpl;

import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.factory.icreate.ICreateDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

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
        System.out.println("SessionId:" + appiumDriver.getSessionId() + ", RemoteUrl:" + appiumDriver.getRemoteAddress().toString());
        return appiumDriver;
    }
}
