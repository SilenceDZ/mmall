package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
	ServerResponse<User> login(String username,String password);

	public ServerResponse<String> register(User user);

	ServerResponse<String> checkValid(String str, String type);
	
	ServerResponse<String> selectQuestion(String username);
	
	ServerResponse<String> checkAnswer(String username,String question,String answer);
	
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken); 

	/**
	 *Title:resetPassword
	 *Description:登录状态下的重置密码，为了防止横向越权，要验证用户和密码
	 *	因为查询的是一个count，如果不指定用户id，查询出来count>0，一定会为true
	 *@param passwordOld
	 *@param passowrdNew
	 *@param user
	 *@return
	 *Throws
	 */
	public ServerResponse<String> resetPassword(String passwordOld,String passowrdNew,User user);
}
