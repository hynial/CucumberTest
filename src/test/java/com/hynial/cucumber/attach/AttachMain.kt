package com.hynial.cucumber.attach

import io.appium.java_client.AppiumDriver

fun main(args: Array<String>){
    val sid = "70659247-ae04-4fd2-9813-a940beca52e4"
    val appiumDebugHelp = AppiumDebugHelp(sid)
    appiumDebugHelp.artifactClass()

    var appiumDriver = appiumDebugHelp.attachSession()

    //println(appiumDriver.pageSource)

    enjoy(appiumDriver)
    println()
}

fun enjoy(driver: AppiumDriver<*>){

}