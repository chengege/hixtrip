package com.hixtrip.sample.domain.pay.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;

public class PayRecordContext {
    private PayRecordStrategy strategy;

    public void setStrategy(PayRecordStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy(CommandPay commandPay) throws RuntimeException {
        strategy.payRecord(commandPay);
    }

}
