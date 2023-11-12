package com.soft2242.shop.service;

import com.soft2242.shop.entity.UserShippingAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.vo.AddressVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface UserShippingAddressService extends IService<UserShippingAddress> {


    Integer saveShippingAddress(AddressVO addressVO);

    Integer editShoppingAddress(AddressVO addressVO);
}
