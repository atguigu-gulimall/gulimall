## 说明文档

- [centos服务器安装]()
- [安装docker]()
- [安装mysql]()
- [导入sql]()
- [安装redis]()
- [安装nexus]()
- [使用idea插件生成代码]()
- []()





# 谷粒商城

## 架构的模型

1。 调整视频教程中的项目模块组织方式；
2。 每一个模块的parent模块都是springboot，这样做的好处是，当开发团队有多个小组，每一个小组都只负责其中一个模块时，可以让单个小组只需下载自己负责的功能模块即可，不是自己负责的模块，不需要下载。
3。 把其他模块中用到的公共依赖迁移到common工程中去，这样可以使得一些依赖能够被统一管理起来；




## nacos

使用的版本： 1.2.1
使用的springboot版本：  2.3.2.RELEASE
使用的springcloud版本：  Hoxton.SR6
使用的springcloud-alibaba版本： 2.2.1.RELEASE

1。 把coupon模块接入nacos，并利用不同的group和共享配置使用nacos 
2。 创建gateway模块，并接入nacos，同时利用共享配置方式使用nacos


## 把renren-fast注册到nacos注册中心

1. pom中添加 gulimall-common 的依赖
2. 修改springboot的版本： 2.3.2.RELEASE  不修改会报 配置不正确 的错误信息；
3. 配置文件中添加 discovery 的配置信息
4. 启动类上添加 @EnableDiscoveryClient 的注解

## mybatis-plus逻辑删除

1。 挑选数据库表中某一个字段作为逻辑删除字段
2。 在这个表的entity的这个字段上添加  @TableLogic(value = "1", delval = "0") 
3。 重写service


## JSR303




## 参考资料

1。 源代码： https://gitee.com/leifengyang/gulimall
2。 elememt： https://element.eleme.cn/#/zh-CN
3。 spring官网： https://spring.io/projects/spring-cloud
4。 spring旧版本指导手册： https://docs.spring.io/spring-cloud/docs/
5。 如何查看spring旧版文档： https://blog.csdn.net/weixin_42091529/article/details/113694682
6。 mybatis-plus官网： https://baomidou.com/pages/6b03c5/#%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95






