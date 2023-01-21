package cv.pn.sibs.repositories;

import cv.pn.sibs.entities.Item;
import cv.pn.sibs.entities.Order;
import cv.pn.sibs.entities.User;
import cv.pn.sibs.projections.POrderCode;
import cv.pn.sibs.utilities.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByOrderCode(String code);

    Optional<Order> findOrderByOrderCode(String code);
    List<POrderCode> findDistinctByUser(User user);
    Optional<POrderCode> findDistinctByOrderCode(String orderCode);

    List<Order> findAllByUserOrOrderCode(User user, String orderCode);

    List<Order> findAllByOrderStatusOrderByCreationDateAsc(Constants.OrderStatus orderStatus);



    //List<Order> findAllByStatusIsTrueAndItemOrderByCreationDateAsc(Item item);
    //Optional<Order> findByOrderCodeAndItem(String orderCode, Item item);

}