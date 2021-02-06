package com.hynial.wechat.entity.mobiledriver;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class BaseCapabilities {
    public static String APP = "app";
    public static String PLATFORM_NAME = "platformName";
    public static String UDID = "udid";
    public static String DEVICE_NAME = "deviceName";
    public static String AUTOMATION_NAME = "automationName";
    public static String NO_RESET = "noReset";
    public static String FULL_RESET = "fullReset";
    public static String AUTO_WEBVIEW = "autoWebview";
    public static String AUTO_WEBVIEW_TIMEOUT = "autoWebviewTimeout";
    public static String ENSURE_WEBVIEWS_HAVE_PAGES = "ensureWebviewsHavePages";
    public static String RESET_KEYBOARD = "resetKeyboard";
    public static String UNICODE_KEYBOARD = "unicodeKeyboard";
    public static String BROWSER_NAME = "browserName";

    @Getter(AccessLevel.PROTECTED) // public can get it
    protected String udid;

    @Getter(AccessLevel.PROTECTED)
    protected String platformName;

    @Getter(AccessLevel.PROTECTED)
    protected String deviceName;

    @Getter(AccessLevel.PROTECTED)
    protected String platformVersion;
    @Getter(AccessLevel.PROTECTED)
    protected String automationName;
    @Getter(AccessLevel.PROTECTED)
    protected String appPath;

    protected String browserName = "Browser";

    protected boolean clearSystemFiles;
    protected boolean autoAcceptAlerts;
    protected boolean noReset = true;
    protected boolean fullReset = false;
    protected boolean autoWebview = false;

    protected boolean ensureWebviewsHavePages = false;
    protected int autoWebviewTimeout = 30000;

    protected boolean resetKeyboard = true;
    protected boolean unicodeKeyboard = true;
}
