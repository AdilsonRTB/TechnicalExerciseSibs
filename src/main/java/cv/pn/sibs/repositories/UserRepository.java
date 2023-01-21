package cv.pn.sibs.repositories;

import cv.pn.sibs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByEmailLikeOrNameLike(String email, String name);

    Optional<User> findByEmail(String email);
}