package com.soft2242.shop.service;

import com.soft2242.shop.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface CategoryService extends IService<Category> {

    List<Category> getIndexCategoryList();

    List<CategoryVO> getCategoryList();
}
