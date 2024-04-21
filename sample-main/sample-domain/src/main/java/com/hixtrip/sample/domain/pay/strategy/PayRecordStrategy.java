package com.hixtrip.sample.domain.pay.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;

public interface PayRecordStrategy {
    void payRecord(CommandPay commandPay) throws RuntimeException;
}
