package com.hynial.cucumber.attach

import io.appium.java_client.AppiumDriver

fun main(args: Array<String>){
    val sid = "11cedc22-e084-4194-abfc-f33d1ccbcb82"
    val appiumDebugHelp = AppiumDebugHelp(sid)
//    appiumDebugHelp.checkClassLoaded()
    appiumDebugHelp.artifactClass()

    var appiumDriver = appiumDebugHelp.attachSession()

    enjoy(appiumDriver)
    println()
}

fun enjoy(driver: Any){
    val appiumDriver = driver as AppiumDriver<*>
    println(appiumDriver.pageSource)
}