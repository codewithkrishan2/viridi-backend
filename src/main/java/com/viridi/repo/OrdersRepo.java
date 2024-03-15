package com.viridi.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viridi.entity.Orders;
import com.viridi.enums.OrderStatus;

public interface OrdersRepo extends JpaRepository<Orders, Long> {

	Orders findByUserIdAndStatus(Long userId, OrderStatus pending);
	
	List<Orders> findAllByStatusIn(List<OrderStatus> orderStatusList);
	
	List<Orders> findByUserIdAndStatusIn(Long userId, List<OrderStatus> orderStatusList);

	Optional<Orders> findByTrackingId(UUID trackingId);

	List<Orders> findByOrderedDateBetweenAndStatus(Date startOfMonth, Date endOfMonth, OrderStatus status);

	Long countByStatus(OrderStatus status);

}
