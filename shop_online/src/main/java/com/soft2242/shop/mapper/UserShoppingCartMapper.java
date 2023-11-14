package com.soft2242.shop.mapper;

import com.soft2242.shop.entity.UserShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soft2242.shop.vo.CartGoodsVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
public interface UserShoppingCartMapper extends BaseMapper<UserShoppingCart> {
List<CartGoodsVO> getCartGoodsInfo(@Param("id") Integer id);

}
