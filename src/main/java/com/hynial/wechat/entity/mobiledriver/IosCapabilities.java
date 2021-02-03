package com.hynial.wechat.entity.mobiledriver;

import lombok.Data;

@Data
public class IosCapabilities extends BaseCapabilities{
    private String xcodeOrgId;
    private String xcodeSigningId;
}
