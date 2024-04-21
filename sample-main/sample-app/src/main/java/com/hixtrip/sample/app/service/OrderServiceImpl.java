package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.order.vo.OrderVO;
import com.hixtrip.sample.client.order.vo.PayCallbackVO;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private PayDomainService payDomainService;

    @Autowired
    private InventoryDomainService inventoryDomainService;

    @Autowired
    private CommodityDomainService commodityDomainService;

    @Override
    public OrderVO createOrder(CommandOderCreateDTO commandOderCreateDTO) {

        String skuId = commandOderCreateDTO.getSkuId();
        // 库存校验
        Integer inventory = inventoryDomainService.getInventory(skuId);
        if (inventory <= 0) {
            throw new RuntimeException("库存不足");
        }

        // 库存预占
        if(inventoryDomainService.changeInventory(skuId,0L, Long.valueOf(commandOderCreateDTO.getAmount()),0L)){
            throw new RuntimeException("库存不足");
        }

        BigDecimal skuPrice = commodityDomainService.getSkuPrice(skuId);

        // 创建订单
        Order order = Order.builder()
                .id("") // 分布式自增id生成
                .userId(commandOderCreateDTO.getUserId())
                .skuId(commandOderCreateDTO.getSkuId())
                .amount(commandOderCreateDTO.getAmount())
                .money(skuPrice)
                .payStatus("0") // 未支付
                .createBy(commandOderCreateDTO.getUserId())
                .build();

         orderDomainService.createOrder(order);
        return OrderVO.builder().id("id")
                .code("200")
                .msg("OK")
                .build();
    }

    @Override
    public PayCallbackVO payCallback(CommandPayDTO commandPayDTO) {
        CommandPay commandPay = CommandPay.builder()
                                        .orderId(commandPayDTO.getOrderId())
                                        .payStatus(commandPayDTO.getPayStatus())
                                        .build();

        try {
            payDomainService.payRecord(commandPay);
        }catch (RuntimeException e){ // 可自定义成业务异常
            return PayCallbackVO.builder()
                    .code("500")
                    .code("重复支付")
                    .build();
        }
        return PayCallbackVO.builder()
                .code("200")
                .code("OK")
                .build();
    }
}
