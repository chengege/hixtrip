package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.order.vo.OrderVO;
import com.hixtrip.sample.client.order.vo.PayCallbackVO;

/**
 * 订单的service层
 */
public interface OrderService {

    /**
     * 下单
     * @param commandOderCreateDTO
     * @return
     */
    OrderVO createOrder(CommandOderCreateDTO commandOderCreateDTO);

    /**
     * 支付记录
     * @param commandPayDTO
     * @return
     */
    PayCallbackVO payCallback(CommandPayDTO commandPayDTO);

}
