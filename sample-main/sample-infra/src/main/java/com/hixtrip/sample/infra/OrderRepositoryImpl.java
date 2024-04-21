package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void insert(Order order) {
        orderMapper.insert(OrderDOConvertor.INSTANCE.doToEntry(order));
    }

    @Override
    public Order findById(String orderId) {
        return OrderDOConvertor.INSTANCE.doToDomain(orderMapper.findById(orderId));
    }

    @Override
    public void updatePayStatus(Order order) {
        orderMapper.updateById(OrderDOConvertor.INSTANCE.doToEntry(order));
    }

}
