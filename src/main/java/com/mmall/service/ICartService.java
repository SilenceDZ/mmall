package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

public interface ICartService {
	
	/**
	 *Title:add
	 *Description:购物车增加商品
	 *@param userId 用户id
	 *@param productId 商品id
	 *@param count 增加数量
	 *@return
	 *Throws
	 */
	public ServerResponse add(Integer userId,Integer productId,Integer count);
	
	/**
	 *Title:update
	 *Description:更新购物车某个产品数量
	 *@param userId
	 *@param productId
	 *@param count
	 *@return
	 *Throws
	 */
	public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);
	
	public ServerResponse<CartVo> deleteProduct(String productIds,Integer userId);

	public ServerResponse list(Integer userId);
	
	public ServerResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked);
	
	public ServerResponse<Integer> getCartProductCount(Integer userId);
}
