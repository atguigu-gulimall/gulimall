package com.atguigu.gulimall.coupon.service;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 12:03:57
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

