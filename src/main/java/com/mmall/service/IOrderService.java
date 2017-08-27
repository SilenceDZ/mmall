package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface IOrderService {
	
	/**
	 *Title:pay
	 *Description:通过支付宝接口进行支付
	 *@param userId 用户ID
	 *@param orderNo 订单号
	 *@param path 付款二维码保存路径
	 *@return
	 *Throws
	 */
	public ServerResponse pay(Integer userId,Long orderNo,String path);
}
