package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;

@Controller
@RequestMapping("/cart/")
public class CartController{
	
	@Autowired
	private ICartService iCartService;
	
	@RequestMapping("list.action")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpSession session){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.list(user.getId());
	}
	
	@RequestMapping("add.action")
	@ResponseBody
	public ServerResponse<CartVo> add(HttpSession session,Integer count,
			Integer productId){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.add(user.getId(), productId, count);
	}
	
	@RequestMapping("update.action")
	@ResponseBody
	public ServerResponse<CartVo> update(HttpSession session,Integer count,
			Integer productId){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.update(user.getId(), productId, count);
	}
	
	@RequestMapping("delete_product.action")
	@ResponseBody
	public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.deleteProduct(productIds, user.getId());
	}
	//全选
	//全反选
	@RequestMapping("select_all.action")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), null,Const.Cart.CHECKED);
	}
	
	@RequestMapping("un_select_all.action")
	@ResponseBody
	public ServerResponse<CartVo> unSelectAll(HttpSession session){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.UN_CHECKED);
	}
	//单独选
	//单独反选
	@RequestMapping("select.action")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session,Integer productId){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(), productId,Const.Cart.CHECKED);
	}
	
	@RequestMapping("un_select.action")
	@ResponseBody
	public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.UN_CHECKED);
	}
	//查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
	@RequestMapping("get_cart_product_count.action")
	@ResponseBody
	public ServerResponse<Integer> getCartProductCount(HttpSession session){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createBySuccess(0);
		}
		return iCartService.getCartProductCount(user.getId());
	}
	
}
