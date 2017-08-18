package com.mmall.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

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
		
		//TODO 密码登录MD5
		User user = userMapper.selectLogin(username, password);
		if(user==null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功",user);
	}

}
