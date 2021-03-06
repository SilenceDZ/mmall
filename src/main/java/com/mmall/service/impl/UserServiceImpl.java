package com.mmall.service.impl;

import java.util.UUID;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
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

	@Override
	public ServerResponse<String> selectQuestion(String username) {
		//检验用户名是否存在
		ServerResponse checkValid = this.checkValid(username, Const.USERNAME);
		if(checkValid.isSuccess()){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if(StringUtils.isNotBlank(question)){
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("找回的密码问题是空的");
	}

	@Override
	public ServerResponse<String> checkAnswer(String username,
			String question, String answer) {
		int resultCount=userMapper.checkAnswer(username, question, answer);
		if(resultCount>0){
			//问题及答案是这个用户，并且是正确的
			String forgetToken=UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题答案错误");
	}

	@Override
	public ServerResponse<String> forgetResetPassword(String username,
			String passwordNew, String forgetToken) {
		if(StringUtils.isNotBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("参数错误，token需要传递");
		}
		ServerResponse checkValid = this.checkValid(username, Const.USERNAME);
		if(checkValid.isSuccess()){
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
		if(StringUtils.isBlank(token)){
			return ServerResponse.createByErrorMessage("token无效或者过期");
		}
		if(StringUtils.equals(forgetToken, token)){
			String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount=userMapper.updatePasswordByUsername(md5Password, passwordNew);
			if(rowCount>0){
				return ServerResponse.createBySuccessMessage("修改密码成功");
			}
		}else {
			return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码token");
		}
		
		return ServerResponse.createByErrorMessage("修改密码失败");
	}

	@Override
	public ServerResponse<String> resetPassword(String passwordOld,
			String passwordNew, User user) {
		int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
		if(resultCount==0){
			return ServerResponse.createByErrorMessage("旧密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		int updateCount = userMapper.updateByPrimaryKeySelective(user);
		if(updateCount>0){
			return ServerResponse.createByErrorMessage("密码更新成功");
		}
		return ServerResponse.createByErrorMessage("密码更新失败");
	}

	@Override
	public ServerResponse<User> updateInformation(User user) {
		//username 不能被更新
		//email也要校验，看有无改动。要和id关联。
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resultCount>0){
			return ServerResponse.createByErrorMessage("email已被注册，请更换email重试");
		}
		User updateUser=new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if(updateCount>0){
			return ServerResponse.createBySuccess("更新成功",updateUser);
		}
		return ServerResponse.createByErrorMessage("更新失败");
	}

	@Override
	public ServerResponse<User> getInfomation(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if(user==null){
			return ServerResponse.createByErrorMessage("找不到用户");
		}
		//密码不能对外暴露
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}

	@Override
	public ServerResponse checkAdminRole(User user) {
		if(user!=null && user.getRole().intValue()==Const.Role.ROLE_ADMIN){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
	
}
