package kz.seisen.buysellforex.repository;

import kz.seisen.buysellforex.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}