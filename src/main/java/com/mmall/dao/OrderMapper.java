package com.mmall.dao;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId,@Param("orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);
}