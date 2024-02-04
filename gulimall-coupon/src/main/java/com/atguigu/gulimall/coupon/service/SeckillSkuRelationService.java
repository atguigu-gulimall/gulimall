package com.atguigu.gulimall.coupon.service;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 12:03:57
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

