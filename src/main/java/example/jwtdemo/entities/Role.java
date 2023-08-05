package example.jwtdemo.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	private String roleName;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	public Role(String roleName, User user) {
		this.roleName = roleName;
		this.user = user;
	}
	
	
}
