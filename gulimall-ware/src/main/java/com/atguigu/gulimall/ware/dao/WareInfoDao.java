package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author zeanzai
 * @email zeanzai.me@gmail.com
 * @date 2023-08-25 11:22:06
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
