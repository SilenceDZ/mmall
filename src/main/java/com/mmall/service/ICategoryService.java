package com.mmall.service;

import java.util.List;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

public interface ICategoryService {
	public ServerResponse addCategory(String categoryName,Integer parentId);

	public ServerResponse updateCategoryName(Integer categoryId,String categoryName);
	
	public ServerResponse<List<Category>> getchildrenParallCategory(Integer categoryId);

	public ServerResponse selectCategoryAndChildrenById(Integer categoryId) ;
}
