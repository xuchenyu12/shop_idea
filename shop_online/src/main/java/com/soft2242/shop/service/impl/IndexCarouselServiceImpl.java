package com.soft2242.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft2242.shop.entity.IndexCarousel;
import com.soft2242.shop.mapper.IndexCarouselMapper;
import com.soft2242.shop.service.IndexCarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class IndexCarouselServiceImpl extends ServiceImpl<IndexCarouselMapper, IndexCarousel> implements IndexCarouselService {
    

    @Override
    public List<IndexCarousel> getList(Integer distributionSite) {
        //使用wrapper设置查询条件,当distributionSite不为空时,作为条件查询
        //distributionSite为1时,查询首页广告,为2时查询商品分类页广告
        LambdaQueryWrapper<IndexCarousel> queryWrapper=new LambdaQueryWrapper<>();
        if(distributionSite!=null){
            queryWrapper.eq(IndexCarousel::getType,distributionSite);
        }
        //根据创建时间倒序排序
        queryWrapper.orderByDesc(IndexCarousel::getCreateTime);
        //将查询出的广告列表存入list
        List<IndexCarousel> list= baseMapper.selectList(queryWrapper);
        return list;
    }
}

