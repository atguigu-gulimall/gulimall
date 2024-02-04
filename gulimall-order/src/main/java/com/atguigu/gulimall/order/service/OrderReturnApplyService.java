package com.atguigu.gulimall.order.service;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.order.entity.OrderReturnApplyEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 11:56:49
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

