package com.soft2242.shop.convert;

import com.soft2242.shop.entity.UserOrder;
import com.soft2242.shop.vo.OrderDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.core.annotation.Order;

@Mapper
public interface UserOrderDetailConvert {
    UserOrderDetailConvert INSTANCE = Mappers.getMapper(UserOrderDetailConvert.class);

    OrderDetailVO convertToORderDetailVO(UserOrder userOrder);
}
