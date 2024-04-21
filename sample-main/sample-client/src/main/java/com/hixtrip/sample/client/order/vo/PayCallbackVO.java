package com.hixtrip.sample.client.order.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayCallbackVO {
    private String code;
    private String msg;
}
