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
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/manager/category")
public class CategoryManageController {
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private ICategoryService iCategoryService;
	
	@RequestMapping("add_category.action")
	@ResponseBody
	public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0") Integer parentId){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iCategoryService.addCategory(categoryName, parentId);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	@RequestMapping("set_category_name.action")
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			return iCategoryService.updateCategoryName(categoryId, categoryName);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	/**
	 *Title:getChildrenParalleCategory
	 *Description:平行查询节点的品类子节点
	 *@param session
	 *@param categoryId
	 *@return
	 *Throws
	 */
	@RequestMapping("get_category.action")
	@ResponseBody
	public ServerResponse getChildrenParalleCategory(HttpSession session,
			@RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//查询子节点的category信息，并且不递归，保持平级
			return iCategoryService.getchildrenParallCategory(categoryId);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
	
	@RequestMapping("get_deep_category.action")
	@ResponseBody
	public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,
			@RequestParam(value="categoryId",defaultValue="0")Integer categoryId){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
		}
		//校验一下是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//查询当前节点的id和递归子节点的id
			return iCategoryService.selectCategoryAndChildrenById(categoryId);
		}
		return ServerResponse.createByErrorMessage("无操作权限，需要管理员权限");
	}
}
