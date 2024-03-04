package com.viridi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Orders;
import com.viridi.enums.OrderStatus;

public interface OrdersRepo extends JpaRepository<Orders, Long> {

	Orders findByUserIdAndStatus(Long userId, OrderStatus pending);

}
