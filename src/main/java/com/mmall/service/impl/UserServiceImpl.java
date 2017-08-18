package com.mmall.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if(resultCount==0){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		
		String md5Password=MD5Util.MD5EncodeUtf8(password);
		
		User user = userMapper.selectLogin(username, md5Password);
		if(user==null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功",user);
	}
	
	@Override
	public ServerResponse<String> register(User user){
		ServerResponse<String> checkValid = this.checkValid(user.getUsername(), Const.USERNAME);
		if(!checkValid.isSuccess()){
			return checkValid;
		}
		checkValid = this.checkValid(user.getEmail(), Const.EMAIL);
		if(!checkValid.isSuccess()){
			return checkValid;
		}
		
		
		user.setRole(Const.Role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int resultCount=userMapper.insert(user);
		if(resultCount==0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createByErrorMessage("注册成功");
	}
	
	/**
	 *Title:checkValid
	 *Description:根据type传入的值，校验username或者email.
	 *@param str
	 *@param type
	 *@return
	 *Throws
	 */
	@Override
	public ServerResponse<String> checkValid(String str,String type){
		if(StringUtils.isNotBlank(type)){
			//开始校验
			if(Const.USERNAME.equals(type)){
				int resultCount = userMapper.checkUsername(str);
				if(resultCount>0){
					return ServerResponse.createByErrorMessage("用户已存在");
				}
			}
			if(Const.EMAIL.equals(type)){
				int resultCount =userMapper.checkEmail(str);
				if(resultCount>0){
					return ServerResponse.createByErrorMessage("email已经存在");
				}
			}
		}else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createByErrorMessage("校验成功");
	}
}
