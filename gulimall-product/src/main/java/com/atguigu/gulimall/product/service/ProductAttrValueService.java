package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * spu属性值
 *
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 11:51:29
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

