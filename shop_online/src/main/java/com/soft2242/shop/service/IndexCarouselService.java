package com.soft2242.shop.service;

import com.soft2242.shop.entity.IndexCarousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface IndexCarouselService extends IService<IndexCarousel> {

    /**
     * 首页轮播图查询
     * @param distributionSite
     * @return
     */
    List<IndexCarousel> getList(Integer distributionSite);
}

