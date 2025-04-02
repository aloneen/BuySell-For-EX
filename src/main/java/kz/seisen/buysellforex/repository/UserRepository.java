package kz.seisen.buysellforex.repository;

import kz.seisen.buysellforex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}