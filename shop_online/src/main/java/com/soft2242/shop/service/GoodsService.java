package com.soft2242.shop.service;

import com.soft2242.shop.common.result.PageResult;
import com.soft2242.shop.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.query.Query;
import com.soft2242.shop.query.RecommendByTabGoodsQuery;
import com.soft2242.shop.vo.GoodsVO;
import com.soft2242.shop.vo.IndexTabRecommendVO;
import com.soft2242.shop.vo.RecommendGoodsVO;

import java.util.Queue;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface GoodsService extends IService<Goods> {

    IndexTabRecommendVO getTabRecommendGoodsByTabId(RecommendByTabGoodsQuery query);

    PageResult<RecommendGoodsVO> getRecommendGoodsByPage(Query query);

    GoodsVO getGoodsDetail(Integer id);
}
