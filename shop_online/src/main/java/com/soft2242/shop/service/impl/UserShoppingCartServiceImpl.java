package com.soft2242.shop.service.impl;

import com.soft2242.shop.common.exception.ServerException;
import com.soft2242.shop.entity.Category;
import com.soft2242.shop.entity.Goods;
import com.soft2242.shop.entity.UserShoppingCart;
import com.soft2242.shop.mapper.CategoryMapper;
import com.soft2242.shop.mapper.GoodsMapper;
import com.soft2242.shop.mapper.UserShoppingCartMapper;
import com.soft2242.shop.query.CartQuery;
import com.soft2242.shop.service.UserShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft2242.shop.vo.CartGoodsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
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
public class UserShoppingCartServiceImpl extends ServiceImpl<UserShoppingCartMapper, UserShoppingCart> implements UserShoppingCartService {
    private  final GoodsMapper goodsMapper;
    @Override
    public CartGoodsVO addShopCart(CartQuery query) {
        //
        Goods goods = goodsMapper.selectById(query.getId());
        if (goods==null){
            throw  new ServerException("商品不存在");
        }if (query.getCount()>goods.getInventory()){
            throw new ServerException("商品庫存不足");
        }
        //
        UserShoppingCart userShoppingCart =new UserShoppingCart();
        userShoppingCart.setUserId(query.getUserId());
        userShoppingCart.setGoodsId(goods.getId());
        userShoppingCart.setPrice(goods.getPrice());
        userShoppingCart.setCount(query.getCount());
        userShoppingCart.setAttrsText(query.getAttrsText());
        userShoppingCart.setSelected(false);
        baseMapper.insert(userShoppingCart);

        //返回
        CartGoodsVO goodsVO =new CartGoodsVO();
        goodsVO.setId(userShoppingCart.getId());
        goodsVO.setName(goods.getName());
        goodsVO.setAttrsText(query.getAttrsText());
        goodsVO.setPrice(userShoppingCart.getPrice());
        goodsVO.setNowPrice(goods.getPrice());
        goodsVO.setSelected(userShoppingCart.getSelected());
        goodsVO.setStock(goods.getInventory());
        goodsVO.setCount(query.getCount());
        goodsVO.setPicture(goods.getCover());
        goodsVO.setDiscount(goods.getDiscount());

        return goodsVO;
    }
    //
    @Override
    public List<CartGoodsVO> shopCartList(Integer userId){
        List<CartGoodsVO> list=baseMapper.getCartGoodsInfo(userId);
        return list;
    }
}