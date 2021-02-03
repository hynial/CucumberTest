package com.hynial.wechat.entity.mobiledriver;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppiumInfo {
    public static String APPIUM_HOST = "appiumHost";
    public static String APPIUM_PORT = "appiumPort";

    private String appiumHost;
    private String appiumPort;

    private String appiumJsPath;
    private String appiumLogLevel;
    private String useJSONSource;
    private String realDeviceScreenShot;

    public AppiumInfo(String appiumHost, String appiumPort) {
        this.appiumHost = appiumHost;
        this.appiumPort = appiumPort;
    }

    public String getAppiumUrl(){
        return String.format("http://%s:%s/wd/hub", this.appiumHost, this.appiumPort);
    }
}
