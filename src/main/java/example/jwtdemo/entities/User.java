package example.jwtdemo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="username")
	private String userName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Role> roles;

	public User(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	
	
}
