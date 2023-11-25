package com.soft2242.shop.service;

import com.soft2242.shop.entity.UserOrderGoods;
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
public interface UserOrderGoodsService extends IService<UserOrderGoods> {
void batchUserOrderGoods(List<UserOrderGoods> list);
}
