package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {
	public ServerResponse saveOrUpdateProduct(Product product);

	public ServerResponse<String> setSaleStatus(Integer productId,Integer status);
	
	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
	
	public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize);
	
	public ServerResponse<PageInfo> searchProduct(String productName,Integer productId
			,Integer pageNum,Integer pageSize);
	
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

	/**
	 *Title:getProductByKeywordCategory
	 *Description:前台商品详情，列表，搜索，动态排序
	 *@param keyword 关键字
	 *@param categoryId 商品Id
	 *@param pageNum 当前页码
	 *@param pageSize 页码显示商品数量
	 *@param orderBy 排序规则
	 *@return
	 *Throws
	 */
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
			Integer categoryId, Integer pageNum, Integer pageSize,
			String orderBy);
}
