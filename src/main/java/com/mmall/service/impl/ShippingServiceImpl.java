package com.mmall.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	@Override
	public ServerResponse add(Integer userId,Shipping shipping){
		//前端没有传递id，这里的id是空的
		shipping.setUserId(userId);
		int insertCount = shippingMapper.insert(shipping);
		if(insertCount>0){
			Map result=Maps.newHashMap();
			result.put("shippingId", shipping.getId());
			return ServerResponse.createBySuccess("新建地址成功", result);
		}
		return ServerResponse.createByErrorMessage("新建地址失败")	;
	}
	@Override
	public ServerResponse<String> del(Integer userId,Integer shippingId ){
		//这里的根据id是根据shipping的主键删除购物车中的东西，如果用户传递一个shippingId过来，只根据这个删除，会造成横向越权错误，即删除了所有
		//用户下的购物车的ShippingID这个商品
//		int resultCount = shippingMapper.deleteByPrimaryKey(shippingId);
		int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
		if(resultCount>0){
			return ServerResponse.createBySuccess("删除地址成功");
		}
		return ServerResponse.createByErrorMessage("删除地址失败");
	}
	
	@Override
	public ServerResponse<String> update(Integer userId,Shipping shipping){
		//防止恶意在shipping传递入假的userId造成横向越权，所以再把登录的userId赋值进去
		shipping.setUserId(userId);
		int insertCount = shippingMapper.updateByShipping(shipping);
		if(insertCount>0){			
			return ServerResponse.createBySuccess("更新地址成功");
		}
		return ServerResponse.createByErrorMessage("更新地址失败")	;
	}
	
	public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
		Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
		if(shipping==null){
			return ServerResponse.createByErrorMessage("无法返回地址");
		}
		return ServerResponse.createBySuccess("查询成功",shipping);
	}
	
	public ServerResponse<PageInfo> list(int pageNum,int pageSize,Integer userId){
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		PageInfo pageInfo=new PageInfo(shippingList);
		return ServerResponse.createBySuccess(pageInfo);
	}
}
