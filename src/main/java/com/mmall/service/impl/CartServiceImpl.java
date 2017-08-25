package com.mmall.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;

@Service("iCartService")
public class CartServiceImpl implements ICartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	@Override
	public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
		if(productId==null||count==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		Cart cart = cartMapper.selectCartByUserProductId(userId, productId);
		if(cart==null){
			//这个产品不再购物车中，需要新增一个这个产品的记录
			Cart cartItem=new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setProductId(productId);
			cartItem.setUserId(userId);
			cartMapper.insert(cartItem);
		}else{
			//这个产品已经在购物车中了
			count=cart.getQuantity()+count;
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);
	}
	
	
	
	/**
	 *Title:update
	 *Description:更新购物车某个产品数量
	 *@param userId
	 *@param productId
	 *@param count
	 *@return
	 *Throws
	 */
	@Override
	public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
		if(productId==null||count==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		Cart cart = cartMapper.selectCartByUserProductId(userId, productId);
		if(cart==null){
			cart.setQuantity(count);
		}
		cartMapper.updateByPrimaryKeySelective(cart);
		return this.list(userId);
	}
	@Override
	public ServerResponse<CartVo> deleteProduct(String productIds,Integer userId){
		//guva的方法分割
		List<String> productList=Splitter.on(",").splitToList(productIds);
		if(CollectionUtils.isEmpty(productList)){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.deleteByUserIdProductIds(userId, productList);
		return this.list(userId);
	}
	
	@Override
	public ServerResponse list(Integer userId){
		CartVo cartVo=this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	@Override
	public ServerResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked){
		cartMapper.checkedOrUncheckedProduct(userId,null, checked);
		return this.list(userId);
	}
	
	@Override
	public ServerResponse<Integer> getCartProductCount(Integer userId){
		if(userId==null){
			return ServerResponse.createBySuccess(0);
		}
		return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
	}
	
	
	
	private CartVo getCartVoLimit(Integer userId){
		CartVo cartVo=new CartVo();
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVoList=Lists.newArrayList();
		BigDecimal cartTotalPrice=new BigDecimal("0");
		
		if(CollectionUtils.isNotEmpty(cartList)){
			for (Cart cartItem : cartList) {
				CartProductVo cartProductVo=new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserid(cartItem.getUserId());
				cartProductVo.setProductId(cartItem.getProductId());
				Product product = productMapper.selectByPrimaryKey(cartItem.getId());
				if(product!=null){
					cartProductVo.setProudctMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStock(product.getStock());
					//判断库存
					int buyLimitCount=0;
					if(product.getStock()>=cartItem.getQuantity()){
						//库存有充足的时候
						buyLimitCount=cartItem.getQuantity();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
					}else{
						buyLimitCount=product.getStock();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
						//更新购物车的有效库存
						Cart cartForQuantity=new Cart();
						cartForQuantity.setId(cartItem.getId());
						cartForQuantity.setQuantity(cartItem.getQuantity());
						cartMapper.updateByPrimaryKeySelective(cartForQuantity);
					}
					cartProductVo.setQuantity(buyLimitCount);
					//计算某件商品的总价
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity().doubleValue()));
					cartProductVo.setProductChecked(cartItem.getChecked());
				}
				if(cartItem.getChecked()==Const.Cart.CHECKED){
					//如果已经勾选，增加到购物车的总价中
					cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
				}
				cartProductVoList.add(cartProductVo);
			}
		}
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setCartProductVoList(cartProductVoList);
		cartVo.setAllChecked(this.getAllCheckedStatus(userId));
		cartVo.setImageHost(PropertiesUtil.getProperty("http://img.happymmall.com/"));
		return cartVo;
	}
	
	
	private boolean getAllCheckedStatus(Integer userId){
		if(userId==null){
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserid(userId)==0;
	}
	
	
}
