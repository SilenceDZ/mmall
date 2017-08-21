package com.mmall.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private ProductMapper productMapper;
	@Override
	public ServerResponse saveOrUpdateProduct(Product product){
		if(product!=null){
			//判断子图不为空，并将第一张图作为主图
			if(StringUtils.isNoneBlank(product.getSubImages())){
				String []subImageArray=product.getSubImages().split(",");
				if(subImageArray.length>0){
					product.setMainImage(subImageArray[0]);
				}
			}
			//如果产品id不为空，则对应的应该是更新产品，如果为空则代表需要新增产品
			if(product.getId()!=null){//update
				int updateCount = productMapper.updateByPrimaryKey(product);
				if(updateCount>0){//success
					return ServerResponse.createBySuccess("更新产品成功");
				}else{//fail
					return ServerResponse.createBySuccess("更新产品失败");
				}
			}else{//add
				int insertCount = productMapper.insert(product);
				if(insertCount>0){//success
					return ServerResponse.createBySuccess("增加产品成功");
				}else{//fail
					return ServerResponse.createBySuccess("增加产品失败");
				}
			}
		}
		return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
	}
	
	@Override
	public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
		if(productId==null ||status==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode()
					,ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product =new Product();
		product.setId(productId);
		product.setStatus(status);
		int updateCount = productMapper.updateByPrimaryKeySelective(product);
		if(updateCount>0){
			return ServerResponse.createBySuccess("修改产品销售状态成功");
		}
		return ServerResponse.createByErrorMessage("修改产品销售状态失败");
	}
}
