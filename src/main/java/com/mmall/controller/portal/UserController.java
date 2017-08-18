package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import net.sf.jsqlparser.schema.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

/**
 * @author leo
 * @ClassName :UserController
 * @Description:
 * @date:2017年8月18日下午4:56:43
 */
@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private IUserService iUserService;
	/**
	 *Title:login
	 *Description:用户登录
	 *@param username
	 *@param password
	 *@param session
	 *@return
	 *Throws
	 */
	@RequestMapping(value="login.action",method=RequestMethod.POST)
	@ResponseBody//通过配置把返回值自动转换成json对象
	public ServerResponse<User> login(String username,String password,HttpSession session){
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER,response.getData());
		}
		
		return response;
		
	}
	
	@RequestMapping(value="logout.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	@RequestMapping(value="register.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
	}
	
	@RequestMapping(value="checkValid.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkValid(str, type);
	}
	
	
	
	
	
	
}
