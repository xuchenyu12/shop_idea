package com.soft2242.shop.service;

import com.soft2242.shop.common.result.PageResult;
import com.soft2242.shop.entity.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.query.OrderPreQuery;
import com.soft2242.shop.query.OrderQuery;
import com.soft2242.shop.vo.OrderDetailVO;
import com.soft2242.shop.vo.SubmitOrderVO;
import com.soft2242.shop.vo.UserAddressVO;
import com.soft2242.shop.vo.UserOrderVO;

import java.util.List;

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
public OrderDetailVO getOrderDetail(Integer id);
    List<UserAddressVO> getAddressListByUserId(Integer userId, Integer addressId);

    SubmitOrderVO getPreOrderDetail(Integer userId);
    SubmitOrderVO getPreNowOrderDetail(OrderPreQuery query);

    SubmitOrderVO getRepurchaseOrderDetail(Integer id);
    PageResult<OrderDetailVO> getOrderList(OrderQuery query);

}
