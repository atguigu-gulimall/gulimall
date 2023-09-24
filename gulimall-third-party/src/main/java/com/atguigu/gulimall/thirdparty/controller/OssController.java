package com.atguigu.gulimall.thirdparty.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.thirdparty.config.STSConfig;
import com.atguigu.gulimall.thirdparty.resp.STSResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oss")
public class OssController {

    @Autowired
    private STSConfig stsConfig;

    /**
     *
     * @return
     */
    @RequestMapping("/policy")
    public R policy() {

        // 自定义角色会话名称，用来区分不同的令牌，例如可填写为 SessionTest。
        String roleSessionName = "SessionTest";
        // 以下Policy用于限制仅允许使用临时访问凭证向目标存储空间examplebucket下的src目录上传文件。
        // 临时访问凭证最后获得的权限是步骤4设置的角色权限和该Policy设置权限的交集，即仅允许将文件上传至目标存储空间examplebucket下的src目录。
        // 如果policy为空，则用户将获得该角色下所有权限。
        String policy = "{\n" + "    \"Version\": \"1\",\n" + "    \"Statement\": [\n" + "        {\n" + "            \"Effect\": \"Allow\",\n" + "            \"Action\": \"oss:*\",\n" + "            \"Resource\": [\n" + "                \"acs:oss:*:*:gulimall-zeanzai\",\n" + "                \"acs:oss:*:*:gulimall-zeanzai/*\"\n" + "            ]\n" + "        }\n" + "    ]\n" + "}";
        // 设置临时访问凭证的有效时间为3600秒。
        Long durationSeconds = 3600L;
        try {

            DefaultProfile.addEndpoint(stsConfig.getRegion(), "Sts", stsConfig.getEndpoint());

            IClientProfile profile = DefaultProfile.getProfile(stsConfig.getRegion(), stsConfig.getAccessKeyId(), stsConfig.getAccessKeySecret());

            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();

//            request.setSysMethod(MethodType.POST);
            // 适用于Java SDK 3.12.0以下版本。
            //request.setMethod(MethodType.POST);
            request.setRoleArn(stsConfig.getRoleArn());
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);
            final AssumeRoleResponse response = client.getAcsResponse(request);

            STSResp build = STSResp.builder()
                    .accessKeyId(response.getCredentials().getAccessKeyId())
                    .expiration(response.getCredentials().getExpiration())
                    .accessKeySecret(response.getCredentials().getAccessKeySecret())
                    .securityToken(response.getCredentials().getSecurityToken())
                    .requestId(response.getRequestId()).build();

            return new R().put("data", build);
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
        }
        return new R();
    }


}
