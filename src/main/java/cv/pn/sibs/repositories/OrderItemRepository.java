package cv.pn.sibs.repositories;

import cv.pn.sibs.entities.Item;
import cv.pn.sibs.entities.Order;
import cv.pn.sibs.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findByOrder(Order order);

    Optional<OrderItem> findByOrderAndItem(Order order, Item item);
}