package com.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	@Autowired
	private IProductService iProductService;
	
	@RequestMapping("detail.action")
	@ResponseBody
	public ServerResponse<ProductDetailVo> detail(Integer productId){
		return iProductService.getProductDetail(productId);
	}
	
	@RequestMapping("list.action")
	@ResponseBody
	public ServerResponse<PageInfo> list(@RequestParam(value="keyword",required=false)String keyword
			,@RequestParam(value="categoryId",required=false)Integer categoryId
			,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum
			,@RequestParam(value="pageSize",defaultValue="10")Integer pageSize
			,@RequestParam(value="orderBy",defaultValue="")String orderBy){
		return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
	}
			
	
}
