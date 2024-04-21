package com.hixtrip.sample.domain.pay;

import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.pay.strategy.FailPayStrategy;
import com.hixtrip.sample.domain.pay.strategy.PayRecordContext;
import com.hixtrip.sample.domain.pay.strategy.PayRecordStrategy;
import com.hixtrip.sample.domain.pay.strategy.SuccessPayStrategy;
import org.springframework.stereotype.Component;

/**
 * 支付领域服务
 * todo 不需要具体实现, 直接调用即可
 */
@Component
public class PayDomainService {



    /**
     * 记录支付回调结果
     * 【高级要求】至少有一个功能点能体现充血模型的使用。
     */
    public void payRecord(CommandPay commandPay) throws RuntimeException {
        //无需实现，直接调用即可
        PayRecordContext context = new PayRecordContext();

        if(commandPay.paySuccess()){
            context.setStrategy(new SuccessPayStrategy());
        }else {
            context.setStrategy(new FailPayStrategy());
        }

        context.executeStrategy(commandPay);
    }
}
