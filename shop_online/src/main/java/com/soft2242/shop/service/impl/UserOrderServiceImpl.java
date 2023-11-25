package com.soft2242.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft2242.shop.common.exception.ServerException;
import com.soft2242.shop.convert.UserOrderDetailConvert;
import com.soft2242.shop.entity.Goods;
import com.soft2242.shop.entity.UserOrder;
import com.soft2242.shop.entity.UserOrderGoods;
import com.soft2242.shop.entity.UserShippingAddress;
import com.soft2242.shop.enums.OrderStatusEnum;
import com.soft2242.shop.mapper.GoodsMapper;
import com.soft2242.shop.mapper.UserOrderGoodsMapper;
import com.soft2242.shop.mapper.UserOrderMapper;
import com.soft2242.shop.mapper.UserShippingAddressMapper;
import com.soft2242.shop.query.OrderGoodsQuery;
import com.soft2242.shop.service.UserOrderGoodsService;
import com.soft2242.shop.service.UserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft2242.shop.vo.OrderDetailVO;
import com.soft2242.shop.vo.UserOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {
    @Autowired
    private GoodsMapper goodsMapper;


    @Autowired
    private  UserOrderGoodsService userOrderGoodsService;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> cancelTask;
    private UserShippingAddressMapper userShippingAddressMapper;

    private UserOrderGoodsMapper userOrderGoodsMapper;

    @Async
    public void schedulorderCancel(UserOrder userOrder){
        cancelTask = executorService.schedule(() -> {
            if (userOrder.getStatus() == OrderStatusEnum.WAITING_FOR_PAYMENT.getValue()){
                userOrder.setStatus(OrderStatusEnum.CANCELLED.getValue());
                baseMapper.updateById(userOrder);
            }
        },30, TimeUnit.MINUTES);
    }
    public void cancelScheduledTask(){
        if (cancelTask != null && !cancelTask.isDone()){
            cancelTask.cancel(true);
        }
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public Integer addGoodsOrder(UserOrderVO orderVO) {
        BigDecimal totalPrice  =new BigDecimal(0);
        Integer totalCount= 0;
        BigDecimal totalFreight = new BigDecimal(0);
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(orderVO.getUserId());
        userOrder.setAddressId(orderVO.getAddressId());

        userOrder.setOrderNumber(UUID.randomUUID().toString());
        userOrder.setDeliveryTimeType(orderVO.getDeliveryType());

        userOrder.setStatus(OrderStatusEnum.WAITING_FOR_REVIEW.getValue());
        if (orderVO.getBuyerMessage() != null){
            userOrder.setBuyerMessage(orderVO.getBuyerMessage());
        }
        userOrder.setPayType(orderVO.getPayType());
        userOrder.setPayChannel(orderVO.getPayChannel());
        baseMapper.insert(userOrder);
        schedulorderCancel(userOrder);
        List<UserOrderGoods> orderGoodsList = new ArrayList<>();
        for (OrderGoodsQuery goodsVO : orderVO.getGoods()){
            Goods goods = goodsMapper.selectById(goodsVO.getId());
            if (goodsVO.getCount()>goods.getInventory()){
                throw new ServerException(goods.getName()+"库存数量不足");
            }
            UserOrderGoods userOrderGoods = new UserOrderGoods();
            userOrderGoods.setGoodsId(goods.getId());
            userOrderGoods.setName(goods.getName());
            userOrderGoods.setCover(goods.getCover());
            userOrderGoods.setOrderId(userOrder.getId());
            userOrderGoods.setCount(goodsVO.getCount());
            userOrderGoods.setAttrsText(goodsVO.getSkus());
            userOrderGoods.setFreight(goods.getFreight());
            userOrderGoods.setPrice(goods.getPrice());

            BigDecimal freight = new BigDecimal(userOrderGoods.getFreight().toString());
            BigDecimal goodsPrice = new BigDecimal(userOrderGoods.getPrice().toString());
            BigDecimal count = new BigDecimal(userOrderGoods.getCount().toString());

            goods.setInventory(goods.getInventory() - goodsVO.getCount());

            goods.setSalesCount(goodsVO.getCount());
            BigDecimal price = goodsPrice.multiply(count).add(freight);
            totalPrice = totalPrice.add(price);
            totalCount += goodsVO.getCount();
            totalFreight = totalFreight.add(freight);
            orderGoodsList.add(userOrderGoods);
            goodsMapper.updateById(goods);
        }

        userOrderGoodsService.batchUserOrderGoods(orderGoodsList);

        userOrder.setTotalPrice(totalPrice.doubleValue());
        userOrder.setTotalCount(totalCount);
        userOrder.setTotalFreight(totalFreight.doubleValue());
        baseMapper.updateById(userOrder);

        return userOrder.getId();
    }

    @Override
    public OrderDetailVO getOrderDetail(Integer id) {
        UserOrder userOrder = baseMapper.selectById(id);
        if (userOrder == null){
            throw new ServerException("订单信息不存在");
        }
        OrderDetailVO orderDetailVO = UserOrderDetailConvert.INSTANCE.convertToORderDetailVO(userOrder);
        orderDetailVO.setTotalMoney(userOrder.getTotalPrice());

        UserShippingAddress userShippingAddress = userShippingAddressMapper.selectById(userOrder.getAddressId());
        if (userShippingAddress == null){
            throw new ServerException("收货信息不存在");
        }
        orderDetailVO.setReceiverContact(userShippingAddress.getReceiver());
        orderDetailVO.setReceiverMobile(userShippingAddress.getContact());
        orderDetailVO.setReceiverAddress(userShippingAddress.getAddress());

        List<UserOrderGoods> list = userOrderGoodsMapper.selectList(new
                LambdaQueryWrapper<UserOrderGoods>().eq(UserOrderGoods::getOrderId,id));

        orderDetailVO.setSkus(list);
        orderDetailVO.setPayLatestTime(userOrder.getCreateTime().plusMinutes(30));
        if (orderDetailVO.getPayLatestTime().isAfter(LocalDateTime.now())){
            orderDetailVO.getPayLatestTime();
            Duration duration = Duration.between(LocalDateTime.now(),orderDetailVO.getPayLatestTime());
            orderDetailVO.setCountdown(duration.toMillisPart());

        }
        return  orderDetailVO;
    }


}