package com.soft2242.shop.service;

import com.soft2242.shop.entity.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.vo.UserOrderVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface UserOrderService extends IService<UserOrder> {
Integer addGoodsOrder(UserOrderVO orderVO);
}
