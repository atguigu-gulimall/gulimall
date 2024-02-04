package com.atguigu.gulimall.coupon.service;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.SpuBoundsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 12:03:57
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

