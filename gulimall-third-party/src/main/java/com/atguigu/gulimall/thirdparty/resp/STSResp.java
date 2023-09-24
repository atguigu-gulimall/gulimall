package com.atguigu.gulimall.thirdparty.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class STSResp {

    private String expiration;
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String requestId;


}
