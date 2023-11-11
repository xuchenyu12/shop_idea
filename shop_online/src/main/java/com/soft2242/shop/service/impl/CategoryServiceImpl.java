package com.soft2242.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft2242.shop.convert.GoodsConvert;
import com.soft2242.shop.entity.Category;
import com.soft2242.shop.entity.Goods;
import com.soft2242.shop.enums.CategoryRecommendEnum;
import com.soft2242.shop.mapper.CategoryMapper;
import com.soft2242.shop.mapper.GoodsMapper;
import com.soft2242.shop.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft2242.shop.vo.CategoryChildrenGoodsVO;
import com.soft2242.shop.vo.CategoryVO;
import com.soft2242.shop.vo.RecommendGoodsVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
@Service
@AllArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<Category> getIndexCategoryList() {
        LambdaQueryWrapper<Category> queryWrapper= new LambdaQueryWrapper<>();
        //查询首页和分类页都推荐的分类以及在首页推荐的分类
        queryWrapper.eq(Category::getIsRecommend, CategoryRecommendEnum.ALL_RECOMMEND.getValue()).or().eq(Category::getIsRecommend,CategoryRecommendEnum.INDEX_RECOMMEND.getValue());
        queryWrapper.orderByDesc(Category::getCreateTime);
        List<Category> list = baseMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<CategoryVO> getCategoryList() {
        List<CategoryVO> list=new ArrayList<>();
        //查询配置在分类Tab页上的父级子类
        LambdaQueryWrapper<Category> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getIsRecommend,CategoryRecommendEnum.ALL_RECOMMEND.getValue()).or().eq(Category::getIsRecommend,CategoryRecommendEnum.CATEGORY_HOME_RECOMMEND.getValue());
        List<Category> categories=baseMapper.selectList(queryWrapper);
        //查询该分类下的子分类
        LambdaQueryWrapper<Goods> goodsqueryWrapper=new LambdaQueryWrapper<>();
        for (Category category:categories){
            CategoryVO categoryVO=new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(category.getName());
            categoryVO.setIcon(category.getIcon());
            queryWrapper.clear();
            queryWrapper.eq(Category::getParentId,category.getId());
            List<Category> childCategories = baseMapper.selectList(queryWrapper);
            List<CategoryChildrenGoodsVO> categoryChildrenGoodsList = new ArrayList<>();
            //子分类下的商品列表
            for (Category item : childCategories){
                CategoryChildrenGoodsVO childrenGoodsVO=new CategoryChildrenGoodsVO();
                childrenGoodsVO.setId(item.getId());
                childrenGoodsVO.setName(item.getName());
                childrenGoodsVO.setIcon(item.getIcon());
                childrenGoodsVO.setParentId(category.getId());
                childrenGoodsVO.setParentName(category.getName());
                goodsqueryWrapper.clear();
                List<Goods> goodsList = goodsMapper.selectList(goodsqueryWrapper.eq(Goods::getCategoryId,item.getId()));
                List<RecommendGoodsVO> goodsVOList= GoodsConvert.INSTANCE.convertToRecommendGoodsVOList(goodsList);
                childrenGoodsVO.setGoods(goodsVOList);
                categoryChildrenGoodsList.add(childrenGoodsVO);
            }
            categoryVO.setChildren(categoryChildrenGoodsList);
            list.add(categoryVO);
        }
        return list;
    }
}
