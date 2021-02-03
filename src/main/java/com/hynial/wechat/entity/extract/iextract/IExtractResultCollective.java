package com.hynial.wechat.entity.extract.iextract;

import io.appium.java_client.MobileElement;

import java.util.List;

public interface IExtractResultCollective<T> {

    default List<MobileElement> getElements(){
        return null;
    }

    default MobileElement getElement() {
        return null;
    }

    T getValue();
}
