package com.mmall.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
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
	
	@Override
	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
		if(productId==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode()
					,ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product==null){
			return ServerResponse.createByErrorMessage("商品已下架或删除");
		}
		//使用VO实现
		ProductDetailVo productDetailVo=assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	
	private ProductDetailVo assembleProductDetailVo(Product product){
		ProductDetailVo productDetailVo=new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setMainImg(product.getMainImage());
		productDetailVo.setSubImg(product.getSubImages());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());
		
		//imageHost通过读取配置文件的形式来获取，方便以后项目的变迁，例如图片服务器的重新配置
		productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		//parentCategory
		Category category =categoryMapper.selectByPrimaryKey(product.getId());
		if(category==null){
			productDetailVo.setParentId(0);
		}else{
			productDetailVo.setCategoryId(category.getId());
		}
		//createTime
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		//updateTime
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		return productDetailVo;
	}
	@Override
	public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
		//startPage--start
		PageHelper.startPage(pageNum, pageSize);
		//填充自己的sql查询逻辑
		List<Product> productList=productMapper.selectList();
		List<ProductListVo> productListVoList=Lists.newArrayList();
		for(Product productItem:productList){
			ProductListVo productListVo=assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		//pageHelper--收尾
		PageInfo pageResult=new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	private ProductListVo assembleProductListVo(Product product){
		ProductListVo productListVo=new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		productListVo.setMainImage(product.getMainImage());
		productListVo.setPrice(product.getPrice());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setStatus(product.getStatus());
		return productListVo;
	}
	
	@Override
	public ServerResponse<PageInfo> searchProduct(String productName,Integer productId
			,Integer pageNum,Integer pageSize){
		PageHelper.startPage(pageNum, pageSize);
		if(StringUtils.isNoneBlank(productName)){
			productName=new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList=productMapper.selectByNameAndProductId(productName, productId);
		List<ProductListVo> productListVoList=Lists.newArrayList();
		for(Product productItem:productList){
			ProductListVo productListVo=assembleProductListVo(productItem);
			productListVoList.add(productListVo);
		}
		PageInfo pageResult=new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	
	
	
	
	
	
	
}
