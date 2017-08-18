package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author leo
 * @ClassName :UserController
 * @Description:
 * @date:2017年8月18日下午4:56:43
 */
@Controller
@RequestMapping("/user/")
public class UserController {
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
	public Object login(String username,String password,HttpSession session){
		return null;
		
	}
}
