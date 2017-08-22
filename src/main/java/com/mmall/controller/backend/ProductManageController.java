package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IProductService iProductService;
	
	/**
	 *Title:productSave
	 *Description:后台更新或新增商品
	 *@param session session对象
	 *@param product 要更新或新增的商品对象
	 *@return
	 *Throws
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ServerResponse productSave(HttpSession session,Product product){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
					, "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充增加产品的业务逻辑
			return iProductService.saveOrUpdateProduct(product);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	/**
	 *Title:setSaleStatus
	 *Description:商品的上下架，通过修改商品状态实现
	 *@param session
	 *@param productId 要上下架的商品id
	 *@param status 商品的状态
	 *@return
	 *Throws
	 */
	@RequestMapping("set_sale_status.action")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode()
					, "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iProductService.setSaleStatus(productId, status);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	@RequestMapping("detail.action")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session,Integer productId){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), 
					"用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//业务实现
			return iProductService.manageProductDetail(productId);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	@RequestMapping("list.action")
	@ResponseBody
	public ServerResponse getList(HttpSession session
			,@RequestParam(value="pageNum",defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize",defaultValue="10") Integer pageSize){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//业务实现
			return iProductService.getProductList(pageNum, pageSize);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	@RequestMapping("search.action")
	@ResponseBody
	public ServerResponse productSearch(HttpSession session
			,String productName
			,Integer productId
			,@RequestParam(value="pageNum",defaultValue="1") Integer pageNum
			,@RequestParam(value="pageSize",defaultValue="10") Integer pageSize){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//业务实现
			return iProductService.searchProduct(productName, productId, pageNum, pageSize);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
}
