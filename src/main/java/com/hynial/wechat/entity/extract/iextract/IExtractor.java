package com.hynial.wechat.entity.extract.iextract;

import io.appium.java_client.MobileElement;

import java.util.List;

public interface IExtractor<T> {
    void extract();

    default IExtractor<T> execute(){
        extract();
        return this;
    }

    default IExtractor<T> refresh(){
        return execute();
    }

    default List<MobileElement> getElements(){
        return null;
    }

    default MobileElement getElement() {
        return null;
    }

    T getValue();

}
