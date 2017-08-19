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
import com.mysql.jdbc.interceptors.SessionAssociationInterceptor;

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
	
	@RequestMapping(value="check_valid.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkValid(str, type);
	}
	
	@RequestMapping(value="get_user_info.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user!=null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
	}
	
	@RequestMapping(value="forget_get_question.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
	}
	
	@RequestMapping(value="forget_check_answer.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> frogetCheckAnswer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
	}
	
	@RequestMapping(value="forget_reset_password.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
	}
	
	@RequestMapping(value="reset_password.action",method=RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpSession seesion,String passwordOld,String passwordNew){
		User user = (User) seesion.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorMessage("用户名未登录");
		}
		return iUserService.resetPassword(passwordOld, passwordNew, user);
	}
}
