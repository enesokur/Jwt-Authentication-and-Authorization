package example.jwtdemo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.jwtdemo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUserName(String userName);
	public Boolean existsByUserName(String userName);
	public Boolean existsByEmail(String email);
	
}
