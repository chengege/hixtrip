package com.hixtrip.sample.domain.pay.strategy;

import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuccessPayStrategy implements PayRecordStrategy {

    @Autowired
    private OrderDomainService orderDomainService;

    @Override
    public void payRecord(CommandPay commandPay) throws RuntimeException {
        orderDomainService.orderPaySuccess(commandPay);
    }
}
