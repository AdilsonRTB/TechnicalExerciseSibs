package cv.pn.sibs.repositories;

import cv.pn.sibs.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByNameLike(String name);
}