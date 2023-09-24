package com.atguigu.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.atguigu.gulimall.thirdparty.config.STSConfig;
import com.atguigu.gulimall.thirdparty.resp.STSResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OSSTest {

    @Autowired
    private STSConfig stsConfig;

    @Test
    public void contextLoads() {
        System.out.println(stsConfig.getRegion());
    }

    /***
     * 关于实现原理 ： https://help.aliyun.com/zh/oss/developer-reference/use-temporary-access-credentials-provided-by-sts-to-access-oss?spm=a2c4g.11186623.4.1.3f767931bynewk&scm=20140722.H_100624._.ID_100624-OR_rec-V_1#p-osc-r0m-u63
     *
     */
    @Test
    public void step1() {
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

            System.out.println(build);
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
        }

    }


    @Test
    public void testUpload() throws FileNotFoundException {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        // 填写步骤五获取的临时访问密钥AccessKey ID和AccessKey Secret，非阿里云账号AccessKey ID和AccessKey Secret。
        String accessKeyId = "STS.NTbDCCUS7gMGkwJC3nX7J361h";
        String accessKeySecret = "4LMDvzk5TyUKtoLiP1wECzJxHcxHwEi67tzMnRJxa1at";
        // 填写步骤五获取的安全令牌SecurityToken。
        String securityToken = "CAIStQJ1q6Ft5B2yfSjIr5fXD/n3uIwW0I+saVH7pzM7VLhm3POaijz2IHhMfnBhA+savv42m21W7PoSlqJIRoReREvCUcZr8sz5NoxErtOT1fau5Jko1bdHcAr6UmwXta2/SuH9S8ynDZXJQlvYlyh17KLnfDG5JTKMOoGIjpgVAbZ6WRKjP3g8NrUwHAZ5r9IAPnb8LOukNgWQ4lDdF011oAFx+zUdna202Z+b8QGMzg+4mOAIo4X0K5ygKtthNZFtWtbXquV9bfjGyzUCqUoIpqhriK1B8DPD+8OQCl9D6hTBccisq4M+fFIjN/VrRvAU9aOixccV4LKDy97FrD9WJvxQXijlQ4St/dDJAuvBNKxiLe6hYiWXiYzfbMCq7l1+PSIBVhlDft06MWN9EgA8+o1xqxxWEjsagAEOuwf4j99zPTqWL0qvOpl4q2iEfjHzOKYhTQPwCRyWeAXPPc9IQ6CE4tUj5D3Ax5dXI3oDPJ5kfxr0MXf9TBOAyv2HCuAV8lo7iQ/MA2XLflmdRFIeo+/I+pXd1saUjmbG29ZHgzASQHnSKk/4LnOQKCwdWf+Gk0cGkG3+YP8VFSAA";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);
        // 将本地文件exampletest.txt上传至目标存储空间examplebucket下的src目录。
        PutObjectRequest putObjectRequest = new PutObjectRequest("gulimall-zeanzai",
                "xiaomi-test.png",
                new File("D:\\DocHub\\Work\\大型电商--谷粒商城\\资料源码\\docs\\pics\\xiaomi.png"));

        // ObjectMetadata metadata = new ObjectMetadata();
        // 上传文件时设置存储类型。
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // 上传文件时设置读写权限ACL。
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


}
