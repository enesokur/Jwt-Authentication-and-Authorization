package example.jwtdemo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.jwtdemo.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	public Optional<Role> findByRoleName(String roleName);
}
