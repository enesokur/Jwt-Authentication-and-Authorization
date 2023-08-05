package example.jwtdemo.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
	private String jwt;
	private Long id;
	private String Username;
	private String email;
	private List<String> roles;
}
