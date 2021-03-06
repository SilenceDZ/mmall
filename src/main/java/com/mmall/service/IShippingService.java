package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
	public ServerResponse add(Integer userId,Shipping shipping);
	public ServerResponse del(Integer userId,Integer shippingId );
	public ServerResponse update(Integer userId,Shipping shipping);
	public ServerResponse<Shipping> select(Integer userId,Integer shippingId);
	public ServerResponse<PageInfo> list(int pageNum,int pageSize,Integer userId);
}
