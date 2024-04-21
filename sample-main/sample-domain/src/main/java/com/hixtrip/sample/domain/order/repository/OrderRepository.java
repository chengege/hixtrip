package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;

/**
 *
 */
public interface OrderRepository {
    Order findById(String orderId);

    void updatePayStatus(Order order);

    void insert(Order order);
}
