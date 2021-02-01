package com.hynial.cucumber.attach

import io.appium.java_client.AppiumDriver
import io.appium.java_client.AppiumExecutionMethod
import io.appium.java_client.MobileCommand
import io.appium.java_client.internal.JsonToMobileElementConverter
import io.appium.java_client.remote.AppiumCommandExecutor
import io.appium.java_client.remote.AppiumW3CHttpCommandCodec
import javassist.ClassPool
import javassist.CtNewConstructor
import org.openqa.selenium.remote.*
import org.openqa.selenium.remote.html5.RemoteLocationContext
import org.openqa.selenium.remote.internal.JsonToWebElementConverter
import java.lang.reflect.InvocationTargetException
import java.net.URL
import java.util.*
import java.util.function.Consumer

class AppiumDebugHelp() {
    private val APPIUM_DRIVER_FULL_CLASS_NAME = "io.appium.java_client.AppiumDriver"
    private val MOBILE_DRIVER_FULL_CLASS_NAME = "io.appium.java_client.DefaultGenericMobileDriver"
    private val ANDROID_DRIVER_FULL_CLASS_NAME = "io.appium.java_client.android.AndroidDriver"
    private val IOS_DRIVER_FULL_CLASS_NAME = "io.appium.java_client.ios.IOSDriver"

    var sessionId:String = "c4c68dfd-50f3-4dcd-8b4f-1a3a93c62da0"
    var port = "4724"
    var host = "127.0.0.1"
    var platform = "android"

    var automationName = "UiAutomator2"

    init {
        println("debug!")
    }

    constructor(sid: String): this() {
        if (sid == null || sid == ""){}else {
            this.sessionId = sid
        }
    }

    constructor(sid: String, host: String, port: String, platform: String): this(sid){
        this.host = host
        this.port = port
        this.platform = platform
    }

    /**
     * first step before load class, u can put in the testng class's construct
     */
    fun artifactClass(){
        val classPool = ClassPool.getDefault()
        val appiumDriverCtClass = classPool.get(APPIUM_DRIVER_FULL_CLASS_NAME)
        val defaultGenericMobileDriverCtClass = classPool.get(MOBILE_DRIVER_FULL_CLASS_NAME)
        val defaultGenericMobileDriverConstructor = CtNewConstructor.make("public ${defaultGenericMobileDriverCtClass.simpleName}() {super();}", defaultGenericMobileDriverCtClass)
        defaultGenericMobileDriverCtClass.addConstructor(defaultGenericMobileDriverConstructor)
        defaultGenericMobileDriverCtClass.toClass()

        val appiumDriverConstructor = CtNewConstructor.make("public ${appiumDriverCtClass.simpleName}() {}", appiumDriverCtClass)
        appiumDriverCtClass.addConstructor(appiumDriverConstructor)
        appiumDriverCtClass.toClass()

        val androidDriverCtClass = classPool.get(ANDROID_DRIVER_FULL_CLASS_NAME)
        val androidDriverConstructor = CtNewConstructor.make("public ${androidDriverCtClass.simpleName}() {}", androidDriverCtClass)
        androidDriverCtClass.addConstructor(androidDriverConstructor)
        androidDriverCtClass.toClass()

//        val iosDriverCtClass = classPool.get(IOS_DRIVER_FULL_CLASS_NAME)
//        val iosDriverConstructor = CtNewConstructor.make("public ${iosDriverCtClass.simpleName}() {}", iosDriverCtClass)
//        iosDriverCtClass.addConstructor(iosDriverConstructor)
//        iosDriverCtClass.toClass()
        println("finished artifact class")
    }

    /**
     * second step, call entry
     */
    fun attachSession(): AppiumDriver<*> {
        println("attaching")
        val specialDriverClassName = if (platform == "android") {
            ANDROID_DRIVER_FULL_CLASS_NAME
        } else {
            IOS_DRIVER_FULL_CLASS_NAME
        }

        val dr = Class.forName(specialDriverClassName)
        val appiumDriver = dr.getDeclaredConstructor().newInstance() as AppiumDriver<*>
        reInitDriverBySessionId(appiumDriver)

        return appiumDriver
    }

    private fun reInitDriverBySessionId(driver: Any) {
        val remoteAddress = URL("http://$host:$port/wd/hub")
        val appiumDriverClazz = AppiumDriver::class.java
        val remoteWebDriverClazz = appiumDriverClazz.superclass.superclass
        val sessionIdOfDriver = remoteWebDriverClazz.getDeclaredField("sessionId")
        sessionIdOfDriver.setAccessible(true)
        sessionIdOfDriver.set(driver, SessionId(sessionId))

        val cap = DesiredCapabilities()
        cap.setJavascriptEnabled(true)
        cap.setCapability("automationName", automationName)
        cap.setCapability("platformName", platform)
        cap.setCapability("appPackage", "")

        val capabilitiesOfDriver = remoteWebDriverClazz.getDeclaredField("capabilities")
        capabilitiesOfDriver.setAccessible(true)
        capabilitiesOfDriver.set(driver, cap)


        val executor = AppiumCommandExecutor(MobileCommand.commandRepository, remoteAddress)
        val appiumCommandExecutorClass = AppiumCommandExecutor::class.java

        val setCommandCodec = appiumCommandExecutorClass.getDeclaredMethod("setCommandCodec", CommandCodec::class.java)
        setCommandCodec.setAccessible(true)
        setCommandCodec.invoke(executor, AppiumW3CHttpCommandCodec())

        val getAdditionalCommands = appiumCommandExecutorClass.getDeclaredMethod("getAdditionalCommands")
        getAdditionalCommands.setAccessible(true)

        val httpCommandExecutorClazz = appiumCommandExecutorClass.superclass
        val defineCommand = httpCommandExecutorClazz.getDeclaredMethod("defineCommand", String::class.java, CommandInfo::class.java)
        defineCommand.setAccessible(true)
        (getAdditionalCommands.invoke(executor) as Map<String, CommandInfo>).forEach { (K, V) -> defineCommand.invoke(executor, K, V) }

        val setResponseCodec = appiumCommandExecutorClass.getDeclaredMethod("setResponseCodec", ResponseCodec::class.java)
        setResponseCodec.setAccessible(true)
        setResponseCodec.invoke(executor, Dialect.W3C.responseCodec)

        val setCommandExecutor = remoteWebDriverClazz.getDeclaredMethod("setCommandExecutor", CommandExecutor::class.java)
        setCommandExecutor.setAccessible(true)
        setCommandExecutor.invoke(driver, executor)

        val executeMethod = appiumDriverClazz.getDeclaredField("executeMethod")
        executeMethod.setAccessible(true)
        executeMethod.set(driver, AppiumExecutionMethod(driver as AppiumDriver<*>))

        val locationContext = appiumDriverClazz.getDeclaredField("locationContext")
        locationContext.setAccessible(true)
        locationContext.set(driver, RemoteLocationContext(executeMethod.get(driver) as ExecuteMethod))

        val errorHandler = appiumDriverClazz.getDeclaredField("errorHandler")
        errorHandler.setAccessible(true)

        val setErrorHandler = remoteWebDriverClazz.getDeclaredMethod("setErrorHandler", ErrorHandler::class.java)
        setErrorHandler.setAccessible(true)
        setErrorHandler.invoke(driver, errorHandler.get(driver))

        val driverRemoteAddress = appiumDriverClazz.getDeclaredField("remoteAddress")
        driverRemoteAddress.setAccessible(true)
        driverRemoteAddress.set(driver, remoteAddress)

        val setElementConverter = remoteWebDriverClazz.getDeclaredMethod("setElementConverter", JsonToWebElementConverter::class.java)
        setElementConverter.setAccessible(true)
        setElementConverter.invoke(driver, JsonToMobileElementConverter(driver))

    }

    fun checkClassLoaded() {
        val preChecks = Arrays.asList("io.appium.java_client.AppiumDriver", "io.appium.java_client.ios.IOSDriver"
                , "io.appium.java_client.android.AndroidDriver", "io.appium.java_client.DefaultGenericMobileDriver")
        try {
            val m = ClassLoader::class.java.getDeclaredMethod("findLoadedClass", *arrayOf<Class<*>>(String::class.java))
            m.isAccessible = true
            val cl = ClassLoader.getSystemClassLoader()
            preChecks.forEach(Consumer { preCheck: String ->
                var test1: Any? = null
                try {
                    test1 = m.invoke(cl, preCheck)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
                println("preCheck:" + preCheck + " --- " + (test1 != null))
            })
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}
