package com.hynial.wechat.entity.mobiledriver;

import com.hynial.cucumber.util.PropertyUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.remote.DesiredCapabilities;

@Data
@NoArgsConstructor
public class AndroidCapabilities extends BaseCapabilities {
    public static String APP_PACKAGE = "appPackage";
    public static String APP_ACTIVITY = "appActivity";

    public static String ADB_EXEC_TIMEOUT = "adbExecTimeout";
    public static String NEW_COMMAND_TIMEOUT = "newCommandTimeout";

    private int adbExecTimeout = 60000;
    private int newCommandTimeout = 60000;
    private String appPackage;
    private String appActivity;

    public AndroidCapabilities(PropertyUtil propertyUtil) {
        this.udid = propertyUtil.getString(UDID);
        this.appPath = propertyUtil.getString(APP);
        this.appPackage = propertyUtil.getString(APP_PACKAGE);
        this.appActivity = propertyUtil.getString(APP_ACTIVITY);
        this.platformName = propertyUtil.getString(PLATFORM_NAME);
        this.deviceName = propertyUtil.getString(DEVICE_NAME);
        this.automationName = propertyUtil.getString(AUTOMATION_NAME);

        this.adbExecTimeout = propertyUtil.getInt(ADB_EXEC_TIMEOUT);
        this.newCommandTimeout = propertyUtil.getInt(NEW_COMMAND_TIMEOUT);
        this.autoWebviewTimeout = propertyUtil.getInt(AUTO_WEBVIEW_TIMEOUT);

        this.noReset = propertyUtil.getBoolean(NO_RESET);
        this.autoWebview = propertyUtil.getBoolean(AUTO_WEBVIEW);
        this.ensureWebviewsHavePages = propertyUtil.getBoolean(ENSURE_WEBVIEWS_HAVE_PAGES);
        this.unicodeKeyboard = propertyUtil.getBoolean(UNICODE_KEYBOARD);
        this.resetKeyboard = propertyUtil.getBoolean(RESET_KEYBOARD);
    }

    public DesiredCapabilities getDesiredCapabilities(){
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        // 锁屏下打不开APP
//        desiredCapabilities.setCapability(AndroidCapabilities.APP, this.getAppPath());
//        desiredCapabilities.setCapability(AndroidCapabilities.APP_PACKAGE, this.getAppPackage());
//        desiredCapabilities.setCapability(AndroidCapabilities.APP_ACTIVITY, this.getAppActivity());

        desiredCapabilities.setCapability(AndroidCapabilities.PLATFORM_NAME, this.getPlatformName());
        desiredCapabilities.setCapability(AndroidCapabilities.DEVICE_NAME, this.getDeviceName());
        desiredCapabilities.setCapability(AndroidCapabilities.UDID, this.getUdid());
        desiredCapabilities.setCapability(AndroidCapabilities.AUTOMATION_NAME, this.getAutomationName());

        desiredCapabilities.setCapability(AndroidCapabilities.ADB_EXEC_TIMEOUT, this.getAdbExecTimeout());
        desiredCapabilities.setCapability(AndroidCapabilities.NEW_COMMAND_TIMEOUT, this.getNewCommandTimeout());
        desiredCapabilities.setCapability(AndroidCapabilities.NO_RESET, this.isNoReset());

        desiredCapabilities.setCapability(AndroidCapabilities.AUTO_WEBVIEW, this.isAutoWebview());
        desiredCapabilities.setCapability(AndroidCapabilities.AUTO_WEBVIEW_TIMEOUT, this.getAutoWebviewTimeout());
        desiredCapabilities.setCapability(ENSURE_WEBVIEWS_HAVE_PAGES, this.isEnsureWebviewsHavePages());

        desiredCapabilities.setCapability(UNICODE_KEYBOARD, this.isUnicodeKeyboard());
        desiredCapabilities.setCapability(RESET_KEYBOARD, this.isResetKeyboard());

        return desiredCapabilities;
    }

    public String description(){
        StringBuilder stringBuilder = new StringBuilder("Capabilities Description:");
        stringBuilder.append("\n").append(PLATFORM_NAME + ":").append(this.platformName);

        return stringBuilder.toString();
    }
}
