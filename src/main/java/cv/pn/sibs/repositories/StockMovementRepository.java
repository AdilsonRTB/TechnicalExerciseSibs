package cv.pn.sibs.repositories;

import cv.pn.sibs.entities.Item;
import cv.pn.sibs.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockMovementRepository extends JpaRepository<StockMovement, String> {

    Optional<StockMovement> findByItem(Item item);

}