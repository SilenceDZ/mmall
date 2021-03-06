package com.mmall.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
	
	private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	private CategoryMapper categoryMapper;
	
	public ServerResponse addCategory(String categoryName,Integer parentId){
		if(parentId==null|| StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category =new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);//这个分类是可用的
		int rowCount = categoryMapper.insert(category);
		if(rowCount>0){
			return ServerResponse.createBySuccess("添加品类成功");
		}
		return ServerResponse.createByErrorMessage("添加品类失败");
	}

	@Override
	public ServerResponse updateCategoryName(Integer categoryId,
			String categoryName) {
		if(categoryId==null|| StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("更新品类参数错误");
		}
		Category category=new Category();
		category.setName(categoryName);
		category.setId(categoryId);
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if(rowCount>0){
			return ServerResponse.createBySuccess("更新品类名称成功");
		}
		
		return ServerResponse.createByErrorMessage("更新品类名称失败");
	}

	@Override
	public ServerResponse<List<Category>> getchildrenParallCategory(
			Integer categoryId) {
		List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if(CollectionUtils.isEmpty(categoryList)){
			logger.info("未找到当前分类的子分类");
		}
		
		return ServerResponse.createBySuccess(categoryList);
	}

	/**
	 * 递归查询本节点的id及子节点的id
	 */
	@Override
	public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
		//Sets工具是guva缓存中的工具类，下面的Lists相同
		Set<Category> categorySet=Sets.newHashSet();
		findChildCategory(categorySet, categoryId);
		List<Integer> categoryList=Lists.newArrayList();
		if(categoryId!=null){//id不为空一定有值
			for (Category categoryItem : categorySet) {
				categoryList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccess(categoryList);
	}
	
	/**
	 *Title:findChildCategory
	 *Description:递归算法，找出子节点
	 *@return
	 *Throws
	 */
	public Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if(category!=null){
			categorySet.add(category);
		}
		//查找子节点，mybatis如果查询结果为空，不会返回一个null对象
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		//如果categoryList为size=0，就不会进入这个循环
		for (Category categoryItem : categoryList) {
			findChildCategory(categorySet, categoryItem.getId());
		}
		return categorySet;
	}
}
